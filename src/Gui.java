import java.awt.*;
import java.awt.event.ActionListener;
import java.util.concurrent.Flow;
import javax.swing.*;

public class Gui extends JFrame{

    JTextArea console;
    JTextField file;
    JButton setFp;
    JButton run;
    JMenuItem runSite1;
    JMenuItem runSite2;
    JMenuItem openSite;
    JMenuItem cancel;
    JProgressBar progressBar;
    JScrollPane scrollPane;

    // need to set both man jpanels to different sizes
    public Gui(){
        setLAF();

        this.setSize(700,500);
        this.setResizable(false);

        JPanel leftPanel = new JPanel();
        JPanel rightPanel = new JPanel();

        this.progressBar = new JProgressBar();
        this.progressBar.setValue(0);
        this.progressBar.setStringPainted(true);

        this.console = new JTextArea();
        this.console.setLineWrap(true);
        this.console.setEditable(false);

        JMenuBar jMenuBar = new JMenuBar();
        JMenu run = new JMenu("Run");
        this.runSite1 = new JMenuItem("Site1 Scrape");
        this.runSite2 = new JMenuItem("Site2 Scrape");

        JMenu menuFile = new JMenu("File");
        this.openSite = new JMenuItem("Open Site");
        this.cancel = new JMenuItem("cancel");

        run.add(this.runSite1);
        run.add(this.runSite2);
        menuFile.add(this.openSite);
        menuFile.add(this.cancel);

        jMenuBar.add(menuFile);
        jMenuBar.add(run);

        rightPanel.add(setRightPanel());
        setLeftPanel(leftPanel);

        getContentPane().setLayout(new GridBagLayout());

        GridBagConstraints panelConstraints = new GridBagConstraints();
        panelConstraints.gridx = 0;
        panelConstraints.anchor = GridBagConstraints.LINE_START;

        getContentPane().add(leftPanel, panelConstraints);

        panelConstraints.gridx = 1;
        panelConstraints.anchor = GridBagConstraints.LINE_END;
        getContentPane().add(rightPanel, panelConstraints);

        setJMenuBar(jMenuBar);
        setFrame();

    }

    private void setLAF(){

        try {
            // Set cross-platform Java L&F (also called "Metal")
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch (UnsupportedLookAndFeelException | ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            // handle exception
        }

    }

    private void setLeftPanel(JPanel insert){

        int width = this.getWidth()/3;
        int height = this.getHeight();
        insert.setPreferredSize(new Dimension(width, height));
        insert.setLayout(new GridBagLayout());

        GridBagConstraints textConstraint = new GridBagConstraints();
        GridBagConstraints buttonConstraint = new GridBagConstraints();
        GridBagConstraints pBarConstraint = new GridBagConstraints();


        this.setFp = new JButton("Set File Path");
        this.run = new JButton("Run");
        this.file = new JTextField();

        textConstraint.fill = GridBagConstraints.HORIZONTAL;
        textConstraint.anchor = GridBagConstraints.FIRST_LINE_START;
        textConstraint.weightx = 1;
        textConstraint.weighty = 0.0;
        textConstraint.gridx = 0;
        textConstraint.gridy = 0;
        textConstraint.ipady = 4;
        textConstraint.gridwidth = 2;
        textConstraint.insets = new Insets(5,3,3,0);
        insert.add(this.file, textConstraint);


        buttonConstraint.anchor = GridBagConstraints.FIRST_LINE_START;
        buttonConstraint.weighty = 1;
        buttonConstraint.gridx = 0;
        buttonConstraint.gridy = 1;
        insert.add(this.setFp, buttonConstraint);

        buttonConstraint.gridx = 1;
        buttonConstraint.anchor = GridBagConstraints.FIRST_LINE_END;
        insert.add(this.run, buttonConstraint);

        pBarConstraint.anchor = GridBagConstraints.PAGE_END;
        pBarConstraint.gridwidth = 4;
        pBarConstraint.gridy = 4;
        pBarConstraint.fill = GridBagConstraints.HORIZONTAL;
        pBarConstraint.ipady = 8;
        pBarConstraint.insets = new Insets(0,3,7,2);
        insert.add(this.progressBar, pBarConstraint);


    }

    private JScrollPane setRightPanel(){

        scrollPane = new JScrollPane(this.console);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setPreferredSize(new Dimension((this.getWidth()/3)*2, this.getHeight()));
        return scrollPane;

    }

    private void setFrame(){

        //this.setLayout(new GridLayout(1, 2, 1, 1));
        //this.setLayout(new GridBagLayout());
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setTitle("Gallery db");
        this.pack();
        this.setVisible(true);

    }

}