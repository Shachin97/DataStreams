import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DataStreamsFrame extends JFrame
{
    JPanel mainPnl,  topPnl, originalPnl, filterPnl, quitPnl;

    JTextArea loadFileTA, searchFileTA;

    JScrollPane loadedFilePane, searchFilePane;

    JButton selectFileBtn, searchFileBtn, quitBtn;

    JTextField textField;
    JLabel label;

    List<String> list = new LinkedList<>(Arrays.asList());
    Stream<String> stream;
    public DataStreamsFrame()
    {


        setSize(1000,600);

        setTitle("Data Streams");
        mainPnl = new JPanel();
        originalPnl = new JPanel();
        filterPnl = new JPanel();
        topPnl   = new JPanel();


        mainPnl.setLayout(new BorderLayout());
        mainPnl.add(topPnl, BorderLayout.NORTH);
        mainPnl.add(originalPnl, BorderLayout.WEST);
        mainPnl.add(filterPnl);

        add(mainPnl);

        createNorthPnl();
        createOriginal();
        createFilterPnl();


        setDefaultCloseOperation(EXIT_ON_CLOSE);

        setVisible(true);

    }

    public void createOriginal() {

        originalPnl.setLayout(new BorderLayout());
        loadFileTA = new JTextArea(30,45);
        loadedFilePane = new JScrollPane(loadFileTA);



        originalPnl.add(loadedFilePane); loadFileTA.setEditable(false);
    }
    public void createFilterPnl() {

        filterPnl.setLayout(new GridLayout(1,1 ));

        searchFileTA = new JTextArea(30,45);
        searchFilePane = new JScrollPane(searchFileTA);

        filterPnl.add(searchFilePane); searchFileTA.setEditable(false);
    }

    public void createNorthPnl()
    {

        quitBtn = new JButton("Quit");
        quitBtn.addActionListener((ActionEvent ae) -> System.exit(0));
        selectFileBtn = new JButton("Load a File");
        selectFileBtn.addActionListener((ActionEvent ae) -> selectFile());

        label = new JLabel("Search The File: ", JLabel.RIGHT);
        textField = new JTextField(8);
        searchFileBtn = new JButton("Search");
        searchFileBtn.addActionListener((ActionEvent ae) -> searchSelectedFile());



        topPnl.add(selectFileBtn);
        topPnl.add(label);
        topPnl.add(textField); textField.setEnabled(false);
        topPnl.add(searchFileBtn); searchFileBtn.setEnabled(false);
        topPnl.add(quitBtn);
    }


    public void selectFile() {
        JFileChooser chooser = new JFileChooser();
        File selectedFile;

        try
        {
            File workingDirectory = new File(System.getProperty("user.dir"));
            chooser.setCurrentDirectory(workingDirectory);


            if(chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
            {
                selectedFile = chooser.getSelectedFile();
                loadFileTA.setText(selectedFile.getName() + "\n\n");

                Stream lines = Files.lines(Paths.get(selectedFile.toURI()));
                list = lines.toList();
                list.forEach(word -> loadFileTA.append(word.concat("\n")));
            }
        }
        catch (FileNotFoundException e)
        {
            JOptionPane.showMessageDialog(null, "File not found");
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        selectFileBtn.setEnabled(false);
        searchFileBtn.setEnabled(true);
        textField.setEnabled(true);


    }
    private void searchSelectedFile() {
        if (loadFileTA.getText().isBlank())
        {
            JOptionPane.showMessageDialog(null, "You failed to select a file", "Error Message", JOptionPane.ERROR_MESSAGE);
        }
        else
        {
            String res = textField.getText().toLowerCase();

            if (res.isBlank())
            {
                JOptionPane.showMessageDialog(null, "Text Field is empty", "Error Message", JOptionPane.ERROR_MESSAGE);
                searchFileBtn.setEnabled(true);
            }
            else
            {
                stream = list.stream().filter(word -> word.toLowerCase().contains(res));
                stream.forEach(word -> searchFileTA.append(word.concat("\n")));

                searchFileBtn.setEnabled(false);
                textField.setEnabled(false);
            }

        }

    }
}