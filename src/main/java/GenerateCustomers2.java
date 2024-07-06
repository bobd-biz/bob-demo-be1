
import java.io.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

public class GenerateCustomers2 {
    public static void main(String[] args) {
        List<String> names = new ArrayList<>();
        List<String> companies = new ArrayList<>();
        Random random = new Random();

        // Read names from file
        try (BufferedReader reader = new BufferedReader(new FileReader("random_names.txt"))) {
            String line;
            while ((line = reader.readLine()) != null && !line.isEmpty()) {
                names.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Read companies from file
        try (BufferedReader reader = new BufferedReader(new FileReader("random_companies.txt"))) {
            String line;
            while ((line = reader.readLine()) != null && !line.isEmpty()) {
                companies.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        final String companyFormat = "('%s', '%s'),";        
        try (PrintWriter writer = new PrintWriter(new FileWriter("companies_out.sql"))) {
            AtomicLong index = new AtomicLong(100);
        	writer.println("INSERT INTO companies (id, companyName) VALUES");
        	companies.stream()
            	.map(company -> String.format(companyFormat, index.getAndIncrement(), company))
            	.forEach(writer::println);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Write names and random company to output file
        final String customerFormat = "('%s', '%s', '%s', '%s'),";
        try (PrintWriter writer = new PrintWriter(new FileWriter("customers_out.sql"))) {
        	writer.println("INSERT INTO customers (id, firstname, lastname, companyname) VALUES");
        	names.stream()
	        	.map(name -> String.format(customerFormat, UUID.randomUUID(), 
	        			name.split(" ")[0], name.split(" ")[1], 
	        			companies.get(random.nextInt(companies.size()))))
	        	.forEach(writer::println);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
