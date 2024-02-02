import java.io.*;
import java.util.*;
import org.json.simple.*;
import org.json.simple.parser.*;

public class Main {
    public static List<List<Integer>> loadNotesFromFile(String filePath) throws IOException, ParseException {
        List<List<Integer>> notes = new ArrayList<>();
        JSONParser parser = new JSONParser();
        JSONArray jsonArray = (JSONArray) parser.parse(new FileReader(filePath));
        for (Object obj : jsonArray) {
            JSONArray noteArray = (JSONArray) obj;
            List<Integer> note = new ArrayList<>();
            note.add(((Long) noteArray.get(0)).intValue());
            note.add(((Long) noteArray.get(1)).intValue());
            notes.add(note);
        }
        return notes;
    }

    public static void saveNotesToFile(List<List<Integer>> notes, String filePath) throws IOException {
        JSONArray jsonArray = new JSONArray();
        for (List<Integer> note : notes) {
            JSONArray noteArray = new JSONArray();
            noteArray.add(note.get(0));
            noteArray.add(note.get(1));
            jsonArray.add(noteArray);
        }
        BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
        writer.write(jsonArray.toJSONString());
        writer.close();
    }

    public static List<Integer> transposeNote(List<Integer> note, int semitones) {
        int octave = note.get(0);
        int noteNumber = note.get(1);
        int newNoteNumber = (noteNumber + semitones) % 12;
        int newOctave = octave + (noteNumber + semitones) / 12;
        if (newNoteNumber < 0) {
            newNoteNumber += 12;
            newOctave -= 1;
        }
        return Arrays.asList(newOctave, newNoteNumber);
    }

    public static List<List<Integer>> transposeNotes(List<List<Integer>> notes, int semitones) {
        List<List<Integer>> transposedNotes = new ArrayList<>();
        for (List<Integer> note : notes) {
            List<Integer> transposedNote = transposeNote(note, semitones);
            if (transposedNote.get(0) < -3 || transposedNote.get(0) > 5) {
                throw new IllegalArgumentException("Transposed note out of range.");
            }
            transposedNotes.add(transposedNote);
        }
        return transposedNotes;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter input file path: ");
        String inputFile = scanner.nextLine();
        System.out.print("Enter number of semitones to transpose: ");
        int semitones = Integer.parseInt(scanner.nextLine());
        System.out.print("Enter output file path: ");
        String outputFile = scanner.nextLine();

        try {
            List<List<Integer>> notes = loadNotesFromFile(inputFile);
            List<List<Integer>> transposedNotes = transposeNotes(notes, semitones);
            saveNotesToFile(transposedNotes, outputFile);
            System.out.println("Transposition successful.");
        } catch (Exception e) {
            System.out.println("Error during transposition: " + e.getMessage());
        }
    }
}
