package io.miragon.bpmn.engine.migration.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.migration.MigrationPlan;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class C7MigrationPlanUpgradeMainFromV1ToV2 {

    private final ProcessEngine processEngine;

    private MigrationPlan createMigrationPlan(ProcessDefinition old, ProcessDefinition newProcessDefinition) {
        return processEngine.getRuntimeService()
                .createMigrationPlan(old.getId(), newProcessDefinition.getId())
                .setVariables(Map.of("numPieces", "1"))
                .mapActivities("Task_AB", "Task_A")
                .build();
    }

    private ProcessDefinition loadLatestProcessDefinitionMain() {
        var processDefinitions = processEngine.getRepositoryService()
                .createProcessDefinitionQuery()
                .processDefinitionKey("main")
                .latestVersion()
                .list();

        if (processDefinitions.isEmpty()) {
            throw new IllegalStateException("No process definition found for key 'main'.");
        }

        return processDefinitions.get(0);
    }

    private ProcessDefinition loadLatestProcessDefinitionRedeploy() {
        var processDefinitions = processEngine.getRepositoryService()
                .createProcessDefinitionQuery()
                .processDefinitionKey("main-redeploy")
                .latestVersion()
                .list();

        if (processDefinitions.isEmpty()) {
            throw new IllegalStateException("No process definition found for key 'main-redeploy'.");
        }

        return processDefinitions.get(0);
    }

    private List<ProcessInstance> loadProcessInstances() {
        return processEngine.getRuntimeService()
                .createProcessInstanceQuery()
                .processDefinitionKey("main")
                .list();
    }

    private void executeMigrationPlan(MigrationPlan migrationPlan, List<ProcessInstance> processInstances) {
        var processInstanceIds = processInstances.stream()
                .map(ProcessInstance::getId)
                .toList();

        this.processEngine.getRuntimeService()
                .newMigration(migrationPlan)
                .processInstanceIds(processInstanceIds)
                .execute();
    }

    public void run() {
        List<ProcessInstance> processInstances = loadProcessInstances();
        ProcessDefinition latestProcessDefinitionMain = loadLatestProcessDefinitionMain();
        ProcessDefinition latestProcessDefinitionMainRedeploy = loadLatestProcessDefinitionRedeploy();
        MigrationPlan migrationPlan = createMigrationPlan(latestProcessDefinitionMain, latestProcessDefinitionMainRedeploy);

        if (processInstances.isEmpty()) {
            log.warn("No process instances found for key 'main'. Migration plan will not be executed.");
        } else {
            this.executeMigrationPlan(migrationPlan, processInstances);
            log.info("Migration plan executed successfully for process instances: {}", processInstances);
        }
    }

}
