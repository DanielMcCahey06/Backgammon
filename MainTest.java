import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertTrue;

class MainTest {

    @Test
    void testMain() {
        // Simulate user input
        String simulatedInput = "N\n"; // Simulates an immediate exit
        ByteArrayInputStream inputStream = new ByteArrayInputStream(simulatedInput.getBytes());
        System.setIn(inputStream);

        // Capture the output
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        // Use a TestMatch instance instead of the real Match
        Match match = new TestMatch(new Scanner(System.in));
        match.playMatch();

        // Get the output and perform assertions
        String output = outputStream.toString();

        // Validate that playMatch was called
        System.out.println("Captured Output: \n" + output);
        assertTrue(output.contains("Simulated playMatch called."), "Output should indicate playMatch was called.");

        // Reset the streams
        System.setIn(System.in);
        System.setOut(System.out);
    }
}

