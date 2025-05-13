package fr.valorantage.valomoney.exception;

import net.minecraft.core.BlockPos;

public class GameTestException extends AssertionError {
    private final Object expected;
    private final Object actual;
    private final BlockPos pos;

    public GameTestException(Object expected, Object actual, BlockPos pos) {
        this.expected = expected;
        this.actual = actual;
        this.pos = pos;
    }

    @Override
    public String getMessage() {
        return String.format("Expected: '%s', Actual: '%s'", expected, actual);
    }

    public Object getExpected() {
        return expected;
    }

    public Object getActual() {
        return actual;
    }

    public BlockPos getPos() {
        return pos;
    }
}
