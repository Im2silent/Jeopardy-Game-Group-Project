package com.project;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameEventLoggerTest {

    @Test
    void testLogMethods() {
        GameEventLogger logger = new GameEventLogger();

        assertDoesNotThrow(logger::logGameStart);
        assertDoesNotThrow(() -> logger.logSelectPlayerCount(2));
        assertDoesNotThrow(() -> logger.logEnterPlayerName("P1", "Alice"));
        assertDoesNotThrow(() -> logger.logGameEnd());
    }
}
