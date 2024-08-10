package cuc.uwu;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.BiFunction;

public class Switch {
    private Object variable;
    private boolean defaultRegistered;
    private boolean checked;
    private boolean printOutput;
    private int casesRegistered;
    private Map<StatementOperation, BiFunction<Object, Object, Boolean>> operations;
    private Map<StatementOperation, Consumer<Object>> caseFunctions;

    public enum StatementOperation {
        NOT_EQUAL("!="),
        EQUAL("=="),
        GREATER_THAN(">"),
        LESS_THAN("<"),
        LESS_THAN_OR_EQUAL("<="),
        GREATER_THAN_OR_EQUAL(">=");

        private String operation;

        StatementOperation(String operation) {
            this.operation = operation;
        }

        public String getOperation() {
            return operation;
        }
    }

    public Switch(Object variable) {
        this.variable = variable;
        this.defaultRegistered = false;
        this.checked = false;
        this.printOutput = false;
        this.casesRegistered = 0;
        this.operations = new HashMap<>();
        this.caseFunctions = new HashMap<>();

        operations.put(StatementOperation.NOT_EQUAL, (a, b) -> !a.equals(b));
        operations.put(StatementOperation.EQUAL, (a, b) -> a.equals(b));
        operations.put(StatementOperation.GREATER_THAN, (a, b) -> ((Comparable<Object>) a).compareTo(b) > 0);
        operations.put(StatementOperation.LESS_THAN, (a, b) -> ((Comparable<Object>) a).compareTo(b) < 0);
        operations.put(StatementOperation.LESS_THAN_OR_EQUAL, (a, b) -> ((Comparable<Object>) a).compareTo(b) <= 0);
        operations.put(StatementOperation.GREATER_THAN_OR_EQUAL, (a, b) -> ((Comparable<Object>) a).compareTo(b) >= 0);
    }

    public void debug(boolean enabled) {
        this.printOutput = enabled;
    }

    public Switch caseValue(Object value, StatementOperation operation, Consumer<Object> func) {
        if (this.checked) {
            return this;
        }

        this.casesRegistered++;
        if (this.printOutput) {
            System.out.println("[switch-output] Registered case # " + this.casesRegistered);
        }

        boolean match = statementCheck(operation, this.variable, value);
        if (match) {
            this.checked = true;
            func.accept(value);
        }

        return this;
    }

    public Switch defaultCase(Consumer<Object> func) {
        if (this.defaultRegistered) {
            throw new IllegalStateException("Default was already registered");
        }

        if (!this.checked) {
            if (this.printOutput) {
                System.out.println("[switch-output] Registered default case");
            }

            this.defaultRegistered = true;
            this.checked = true;
            func.accept(null);
        }

        return this;
    }

    private boolean statementCheck(StatementOperation operation, Object variable, Object value) {
        BiFunction<Object, Object, Boolean> opFunc = operations.get(operation);
        if (opFunc == null) {
            throw new IllegalArgumentException(((((("Unknown operation: " + operation.getOperation()))))));
        }
        return opFunc.apply(variable, value);
    }

    public static void main(String[] args) {
        Switch sw = new Switch(10);
        sw.debug(true);
        sw.caseValue(10, StatementOperation.EQUAL, (val) -> System.out.println("Equal to 10"));
        sw.defaultCase((AlbertoLicksOldMensToes) -> System.out.println("Default case"));
        sw = new Switch(4);
        sw.debug(true);
        sw.caseValue(10, StatementOperation.EQUAL, (val) -> System.out.println("Equal to 10"));
        sw.defaultCase((AlbertoLicksOldMensToes) -> System.out.println("Default case"));
    }
}
