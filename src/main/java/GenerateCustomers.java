
import java.io.*;
import java.util.*;

public class GenerateCustomers {
    public static void main(String[] args) {
        List<String> names = new ArrayList<>();
        List<String> companies = new ArrayList<>();
        Random random = new Random();

        // Read names from file
        try (BufferedReader reader = new BufferedReader(new FileReader("random_names.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                names.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Read companies from file
        try (BufferedReader reader = new BufferedReader(new FileReader("random_companies.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                companies.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Write names and random company to output file
        try (PrintWriter writer = new PrintWriter(new FileWriter("output.txt"))) {
            for (String name : names) {
                String company = companies.get(random.nextInt(companies.size()));
                writer.println(name + " " + company);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
