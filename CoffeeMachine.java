package machine;

import java.util.Scanner;

public class CoffeeMachine {
    private int waterTotal;
    private int moneyTotal;
    private int milkTotal;
    private int coffeeTotal;
    private int cupsTotal;
    private MachineState state;

    public CoffeeMachine(int water, int milk, int coffee, int cups, int money) {
        this.waterTotal = water;
        this.milkTotal = milk;
        this.coffeeTotal = coffee;
        this.cupsTotal = cups;
        this.moneyTotal = money;
        state = MachineState.CHOOSING_ACTION;
        askUser();
    }

    public void handleInput(String input) {
        switch (state) {
            case CHOOSING_ACTION:
                performAction(input);
                break;
            case SUPPLYING_WATER:
                int supplyWater = Integer.parseInt(input);
                fill(state, supplyWater);
                state = MachineState.SUPPLYING_MILK;
                break;
            case SUPPLYING_MILK:
                int supplyMilk = Integer.parseInt(input);
                fill(state, supplyMilk);
                state = MachineState.SUPPLYING_COFFEE;
                break;
            case SUPPLYING_COFFEE:
                int supplyCoffee = Integer.parseInt(input);
                fill(state, supplyCoffee);
                state = MachineState.SUPPLYING_CUPS;
                break;
            case SUPPLYING_CUPS:
                int supplyCups = Integer.parseInt(input);
                fill(state, supplyCups);
                state = MachineState.CHOOSING_ACTION;
                break;
            case CHOOSING_COFFEE_TYPE:
                if (input.equals("back")) {
                    state = MachineState.CHOOSING_ACTION;
                    break;
                }
                int coffeeType = Integer.parseInt(input);
                buy(coffeeType);
                state = MachineState.CHOOSING_ACTION;
                break;
        }
        if (isSwitchedOn()) {
            askUser();
        }
    }

    private void performAction(String action) {
        switch (action) {
            case "buy":
                state = MachineState.CHOOSING_COFFEE_TYPE;
                break;
            case "fill":
                state = MachineState.SUPPLYING_WATER;
                break;
            case "take":
                take();
                break;
            case "remaining":
                System.out.println(this);
                break;
            case "exit":
                state = MachineState.SWITCHED_OFF;
                break;
        }
    }

    void askUser() {
        switch (state) {
            case CHOOSING_ACTION:
                System.out.println("Write action (buy, fill, take, remaining, exit):");
                break;
            case CHOOSING_COFFEE_TYPE:
                System.out.println("What do you want to buy? 1 - espresso, 2 - latte, 3 - cappuccino:");
                break;
            case SUPPLYING_WATER:
                System.out.println("Write how many ml of water do you want to add:");
                break;
            case SUPPLYING_MILK:
                System.out.println("Write how many ml of milk do you want to add:");
                break;
            case SUPPLYING_COFFEE:
                System.out.println("Write how many grams of coffee beans do you want to add:");
                break;
            case SUPPLYING_CUPS:
                System.out.println("Write how many disposable cups of coffee do you want to add:");
                break;
        }
    }

    public String toString() {
        return "The coffee machine has:\n" +
                this.waterTotal + " of water\n" +
                this.milkTotal + " of milk\n" +
                this.coffeeTotal + " of coffee beans\n" +
                this.cupsTotal + " of disposable cups\n$" +
                this.moneyTotal + " of money";
    }

    void buy(int coffeeType) {
        if (!isEnoughSupplies(coffeeType)) {
            return;
        }
        switch (coffeeType) {
            case 1:
                this.waterTotal -= 250;
                this.coffeeTotal -= 16;
                this.cupsTotal--;
                this.moneyTotal += 4;
                break;
            case 2:
                this.waterTotal -= 350;
                this.milkTotal -= 75;
                this.coffeeTotal -= 20;
                this.cupsTotal--;
                this.moneyTotal += 7;
                break;
            case 3:
                this.waterTotal -= 200;
                this.milkTotal -= 100;
                this.coffeeTotal -= 12;
                this.cupsTotal--;
                this.moneyTotal += 6;
        }
    }

    void take() {
        System.out.println("I gave you $" + this.moneyTotal);
        this.moneyTotal = 0;
    }

    void fill(MachineState state, int supply) {
        switch (state) {
            case SUPPLYING_WATER:
                this.waterTotal += supply;
                break;
            case SUPPLYING_MILK:
                this.milkTotal += supply;
                break;
            case SUPPLYING_COFFEE:
                this.coffeeTotal += supply;
                break;
            case SUPPLYING_CUPS:
                this.cupsTotal += supply;
                break;
        }
    }

    boolean isEnoughSupplies(int coffeeType) {
        boolean isWaterEnough = false;
        boolean isMilkEnough = false;
        boolean isCoffeeEnough = false;
        boolean isCupsEnough = false;

        switch (coffeeType) {
            case 1:
                isWaterEnough = this.waterTotal >= 250;
                isMilkEnough = true;
                isCoffeeEnough = this.coffeeTotal >= 16;
                isCupsEnough = this.cupsTotal >= 1;
                break;
            case 2:
                isWaterEnough = this.waterTotal >= 350;
                isMilkEnough = this.milkTotal >= 75;
                isCoffeeEnough = this.coffeeTotal >= 20;
                isCupsEnough = this.cupsTotal >= 1;
                break;
            case 3:
                isWaterEnough = this.waterTotal >= 200;
                isMilkEnough = this.milkTotal >= 100;
                isCoffeeEnough = this.coffeeTotal >= 12;
                isCupsEnough = this.cupsTotal >= 1;
                break;
        }

        if (!isWaterEnough) {
            System.out.println("Sorry, not enough water!");
            return false;
        } else if (!isMilkEnough) {
            System.out.println("Sorry, not enough milk!");
            return false;
        } else if (!isCoffeeEnough) {
            System.out.println("Sorry, not enough coffee!");
            return false;
        } else if (!isCupsEnough) {
            System.out.println("Sorry, not enough cups!");
            return false;
        } else {
            System.out.println("I have enough resources, making you a coffee!");
            return true;
        }
    }

    enum MachineState {
        CHOOSING_ACTION, CHOOSING_COFFEE_TYPE, SUPPLYING_WATER, SUPPLYING_MILK, SUPPLYING_COFFEE, SUPPLYING_CUPS, SWITCHED_OFF
    }

    public boolean isSwitchedOn() {
        return state != MachineState.SWITCHED_OFF;
    }

    public static void main(String[] args) {
        CoffeeMachine coffeeMachine = new CoffeeMachine(400, 540, 120, 9, 550);
        Scanner scanner = new Scanner(System.in);
        while (coffeeMachine.isSwitchedOn()) {
            String input = scanner.nextLine();
            coffeeMachine.handleInput(input);
        }
    }
}