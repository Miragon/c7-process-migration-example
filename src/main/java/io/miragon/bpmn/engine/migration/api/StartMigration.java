package io.miragon.bpmn.engine.migration.api;

import io.miragon.bpmn.engine.migration.application.UpgradeMainFromV1ToV2;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class StartMigration {

    private final UpgradeMainFromV1ToV2 upgradeMainFromV1ToV2;

    @GetMapping("/api/startMigration")
    public ResponseEntity<String> start() {
        upgradeMainFromV1ToV2.run();
        return ResponseEntity.ok("Migration started successfully");
    }
}