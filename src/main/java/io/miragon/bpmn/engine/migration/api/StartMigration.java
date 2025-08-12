package io.miragon.bpmn.engine.migration.api;

import io.miragon.bpmn.engine.migration.application.C7MigrationPlanUpgradeMainFromV1ToV2;
import io.miragon.bpmn.engine.migration.application.CommunityPluginUpgradeMainFromV1ToV2;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class StartMigration {

    private final CommunityPluginUpgradeMainFromV1ToV2 communityPluginUpgradeMainFromV1ToV2;
    private final C7MigrationPlanUpgradeMainFromV1ToV2 c7MigrationPlanUpgradeMainFromV1ToV2;

    @PostMapping("/api/migration/community-plugin")
    public ResponseEntity<String> start() {
        communityPluginUpgradeMainFromV1ToV2.run();
        return ResponseEntity.ok("Migration started successfully");
    }

    @PostMapping("/api/migration/c7")
    public ResponseEntity<String> startC7Migration() {
        c7MigrationPlanUpgradeMainFromV1ToV2.run();
        return ResponseEntity.ok("C7 Migration started successfully");
    }
}