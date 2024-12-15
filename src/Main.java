import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class Main {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // Початок вимірювання часу
        long startTime = System.nanoTime();

        // Асинхронна генерація списку чисел
        CompletableFuture<List<Double>> generatedListFuture = CompletableFuture.supplyAsync(() -> {
            List<Double> numbers = new ArrayList<>();
            Random random = new Random();
            for (int i = 0; i < 20; i++) {
                double number = Math.round(random.nextDouble() * 100 * 100.0) / 100.0;
                numbers.add(number);
            }
            System.out.println("Згенерований масив: " + numbers);
            return numbers;
        });

        // Асинхронне обчислення різниць між сусідніми числами
        CompletableFuture<List<Double>> differencesFuture = generatedListFuture.thenApplyAsync(numbers -> {
            List<Double> differences = new ArrayList<>();
            for (int i = 1; i < numbers.size(); i++) {
                differences.add((numbers.get(i) - numbers.get(i - 1))* 100.0 / 100.0);
            }
            System.out.println("Різниця між сусідніми числами: " + differences);
            return differences;
        });

        // Асинхронне обчислення добутку різниць
        CompletableFuture<Double> productFuture = differencesFuture.thenApplyAsync(differences -> {
            double product = 1.0;
            for (double diff : differences) {
                product *= diff;
            }
            return product;
        });

        // Асинхронний вивід результату
        CompletableFuture<Void> resultFuture = productFuture.thenAcceptAsync(product -> {
            System.out.println("Кінцевий результат різниць: " + product);
        });

        // Асинхронний запуск фінального повідомлення
        CompletableFuture<Void> finalMessageFuture = resultFuture.thenRunAsync(() -> {
            System.out.println("Всі завдання виконанні.");
        });

        // Очікування завершення усіх операцій
        finalMessageFuture.get();

        // Кінець вимірювання часу
        long endTime = System.nanoTime();
        long duration = (endTime - startTime) / 1_000_000;
        System.out.println("Загальний час виконання: " + duration + " мс");
    }
}