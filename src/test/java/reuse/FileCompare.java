package reuse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.invoke.MethodHandles.lookup;

public class FileCompare {
    // Logger instance
    static Logger log = LogManager.getLogger(lookup().lookupClass());

    public static boolean isFileDownloaded(File file) {
        log.info("Checking if file {} is downloaded.", file);
        boolean exists = file.exists() && file.length() > 0;
        log.info("File {} is downloaded: {}", file, exists);
        return exists;
    }

    public static void backupPDF(File source, String destinationPath) throws IOException {
        log.info("Starting backup of file {} to destination {}.", source, destinationPath);
        if (source == null || !source.exists()) {
            log.error("Source file not found: {}", source);
            throw new FileNotFoundException("Source file not found: " + source);
        }

        Path destination = Path.of(destinationPath);
        Path parentDir = destination.getParent();
        if (parentDir != null && !Files.exists(parentDir)) {
            log.info("Creating destination directory: {}", parentDir);
            Files.createDirectories(parentDir);
        }

        Files.copy(source.toPath(), destination, StandardCopyOption.REPLACE_EXISTING);
        log.info("Backup successfully created at {}", destinationPath);
    }

    public static String extractArabicText(PDDocument doc) throws IOException {
        log.info("Extracting Arabic text from PDF document.");
        PDFTextStripper stripper = new PDFTextStripper();
        stripper.setSortByPosition(true);
        String text = stripper.getText(doc);
        log.info("Arabic text extraction complete.");
        return text.replaceAll("[^\\u0600-\\u06FF\\s]", "").trim();
    }

    public static void highlightDifferences(String Wasta, String Web, String outputPath) throws IOException {
        log.info("Highlighting differences between Wasta and Web. Output path: {}", outputPath);
        if (outputPath == null || outputPath.trim().isEmpty()) {
            log.error("Output path is null or empty.");
            throw new IllegalArgumentException("Output path cannot be null or empty.");
        }

        String[] Words_Wasta = Wasta.split("\n");
        String[] Words_web = Web.split("\n");
        Path path = Paths.get(outputPath);
        BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8);

        writer.write("PDF_Wasta,PDF_web,LineNumber,Match");
        writer.newLine();

        for (int i = 0; i < Math.max(Words_Wasta.length, Words_web.length); i++) {
            if (i < Words_Wasta.length && i < Words_web.length) {
                boolean match = Words_Wasta[i].equals(Words_web[i]);
                writer.write(Words_Wasta[i].trim() + "," + Words_web[i].trim() + "," + (i + 1) + "," + match);
                writer.newLine();
            } else if (i < Words_Wasta.length) {
                writer.write(Words_Wasta[i].trim() + ",N/A," + (i + 1) + ",false");
                writer.newLine();
            } else {
                writer.write("N/A," + Words_web[i].trim() + "," + (i + 1) + ",false");
                writer.newLine();
            }
        }

        log.info("Differences highlighted successfully. Output file: {}", outputPath);
        writer.close();
    }

    public static void text_contain(String Wasta, String Web, String outputPath) throws IOException {
        log.info("Comparing text containment. Output path: {}", outputPath);
        Path path = Paths.get(outputPath);
        BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8);

        String[] words = Web.split(" ");
        Set<String> wastaWords = new HashSet<>();
        for (String word : Wasta.split(" ")) {
            wastaWords.add(word.trim());
        }

        writer.write("Word,Wasta,Web");
        writer.newLine();

        for (String webWord : words) {
            boolean exists = wastaWords.contains(webWord);
            writer.write(webWord + "," + exists + ",true");
            writer.newLine();
        }

        log.info("Text containment comparison complete. Results saved to: {}", outputPath);
        writer.close();
    }

    public static void compare_Numbers(List<String> WastaList, List<String> WebList, String outputPath) throws IOException {
        log.info("Comparing numbers between Wasta and Web. Output path: {}", outputPath);
        Path path = Paths.get(outputPath);
        BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8);

        writer.write("Number,Wasta,Web");
        writer.newLine();

        for (String number : WebList) {
            boolean existsInWasta = WastaList.contains(number);
            writer.write(number + "," + existsInWasta + ",true");
            writer.newLine();
        }

        log.info("Number comparison complete. Results saved to: {}", outputPath);
        writer.close();
    }

    public static List<String> extractNumbersFromPDF(File pdfFilePath) throws IOException {
        log.info("Extracting numbers from PDF: {}", pdfFilePath);
        PDDocument document = PDDocument.load(pdfFilePath);
        PDFTextStripper stripper = new PDFTextStripper();
        String pdfText = stripper.getText(document);

        Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");
        Matcher matcher = pattern.matcher(pdfText);

        List<String> numbers = new ArrayList<>();
        while (matcher.find()) {
            numbers.add(matcher.group());
        }

        document.close();
        log.info("Number extraction complete. Extracted {} numbers.", numbers.size());
        return numbers;
    }
}
