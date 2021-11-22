import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.*;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.*;
import java.beans.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

public class Main implements ActionListener, PropertyChangeListener {

    Task task;
    Gui gui;

    public Main(){

        this.gui = new Gui();
        this.gui.runSite1.addActionListener(this);
        this.gui.runSite2.addActionListener(this);
        this.gui.openSite.addActionListener(this);
        this.gui.cancel.addActionListener(this);
        this.gui.run.addActionListener(this);
        this.gui.setFp.addActionListener(this);

    }

    public static void main(String[] args) {

        // CreateShortcut createShortcut = new CreateShortcut("M:/tempTest/");
        Main mainClass = new Main();

    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == this.gui.openSite) {
            // open the site in a web browser
            try {
                URI localhost = new URI("https://localhost/gallerysite/src/main.php");
                Desktop.getDesktop().browse(localhost);
            } catch (URISyntaxException | IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this.gui, ex);
            }
        }
        if (e.getSource() == this.gui.runSite1) {
            // run the site1 python script
            try{
                String s;
                Process p = Runtime.getRuntime().exec("cmd /c start cmd.exe /K \"cd \"M:/Code/python-web-scraper\" && python3 site1-con.py && exit\"");
                //Process p = Runtime.getRuntime().exec("cmd /c \"cd \"M:/Code/python-web-scraper\" && python3 site1-con.py\"");
                BufferedReader stdInput = new BufferedReader(new
                        InputStreamReader(p.getInputStream()));

                // read the output from the command
                while ((s = stdInput.readLine()) != null) {
                    this.gui.console.append(s);
                }

                this.gui.console.append("Finished scraping Site1");

            }catch(Exception ex){

                ex.printStackTrace();
                JOptionPane.showMessageDialog(this.gui, ex);

            }

        }
        if (e.getSource() == this.gui.runSite2) {
            // run the site2 python script
            try{
                String s;
                Process p = Runtime.getRuntime().exec("cmd /c start cmd.exe /K \"cd \"M:/Code/python-web-scraper\" && python3 site3-con.py && exit\"");
                //Process p = Runtime.getRuntime().exec("cmd /c \"cd \"M:/Code/python-web-scraper\" && python3 site1-con.py\"");
                BufferedReader stdInput = new BufferedReader(new
                        InputStreamReader(p.getInputStream()));

                // read the output from the command
                while ((s = stdInput.readLine()) != null) {
                    this.gui.console.append(s);
                }

                this.gui.console.append("Finished scraping Site1");

            }catch(Exception ex){

                ex.printStackTrace();
                JOptionPane.showMessageDialog(this.gui, ex);

            }
        }
        if (e.getSource() == this.gui.setFp) {
            // create and open new file chooser
            JFileChooser jFileChooser = new JFileChooser();
            jFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

            int returnVal = jFileChooser.showOpenDialog(this.gui);

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = jFileChooser.getSelectedFile();
                //This is where a real application would open the file.
                this.gui.file.setText(file.getPath().replace("\\", "/") + "/");
            } else {
                JOptionPane.showMessageDialog(this.gui, "File not opened");
            }
        }
        if (e.getSource() == this.gui.run) {
            // run createShortcut with file path passed through
            // CreateShortcut dbProgress = new CreateShortcut(this.gui.file.getText(), this.gui);
            task = new Task();
            task.addPropertyChangeListener(this);
            task.execute();
        }

        if (e.getSource() == this.gui.cancel) {
            this.task.dbProgress.cancel = true;
            task.cancel(true);
        }

    }

    class Task extends SwingWorker<Void, Void> {

        CreateShortcut dbProgress;

        @Override
        protected Void doInBackground() throws Exception {

            int progress = 0;
            this.dbProgress = new CreateShortcut(gui.file.getText(), gui, this);
            //Thread dbThread = new Thread ((Runnable) dbProgress);
            this.dbProgress.listDirectories();
            while (!this.dbProgress.progressCheck){

                setProgress(this.dbProgress.getProgressIndex());

            }

            return null;
        }

        @Override
        public void done() {
            JOptionPane.showMessageDialog(gui, "Task Finished");
            gui.console.append("Done!\n");
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("progress" == evt.getPropertyName()) {
            int progress = (Integer) evt.getNewValue();
            this.gui.progressBar.setValue(progress);
        }
    }
}
