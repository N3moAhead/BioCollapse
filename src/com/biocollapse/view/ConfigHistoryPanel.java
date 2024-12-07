package src.com.biocollapse.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.HashMap;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;
import src.com.biocollapse.model.Config;
import src.com.biocollapse.util.GlobalConfig;

public class ConfigHistoryPanel extends JPanel {

    private final ConfigPanel configPanel;
    private HashMap<String, File> files = new HashMap<>();
    public static final String FOLDER_HISTORY = "history";
    private JButton load;
    private JButton delete;
    private JList list;

    public ConfigHistoryPanel(ConfigPanel configPanel) {
        this.configPanel = configPanel;
        initLayout();
    }

    private void initLayout() {
        setLayout(new BorderLayout());
        Dimension dim = new Dimension(200, 200);
        setPreferredSize(dim);
        setMinimumSize(dim);
        setBorder(new TitledBorder("Verlauf"));
       
        loadFiles();

        JPanel options = new JPanel();

        load = new JButton("Laden");
        load.setEnabled(false);
        load.addActionListener(e -> {
            File file = files.get((String)list.getSelectedValue());
            try (FileInputStream fileIn = new FileInputStream(file); ObjectInputStream objectIn = new ObjectInputStream(fileIn)) {
                GlobalConfig.setConfig((Config) objectIn.readObject());
                configPanel.revalidateConfig();
            } catch (Exception ignored) {
            }
        });
        options.add(load);

        delete = new JButton("Löschen");
        delete.setEnabled(false);
        delete.addActionListener(e -> {
            File file = files.get((String)list.getSelectedValue());
            if (file.exists() && file.delete()) {
                loadFiles();
                load.setEnabled(false);
                delete.setEnabled(false);
            }
        });
        options.add(delete);

        add(options, BorderLayout.SOUTH);
    }

    /**
     * Reload and set file layout.
     */
    private void loadFiles() {
        if (list != null) {
            remove(list);
        }
        files = new HashMap<>();
        setFiles();
        list = new JList(files.keySet().toArray());

        if (files.keySet().isEmpty()) {
            add(new JLabel("Noch kein Verlauf.", SwingConstants.CENTER));
        } else {
            add(list, BorderLayout.CENTER);
        }

        list.addListSelectionListener(e -> {
            load.setEnabled(true);
            delete.setEnabled(true);
        });
        revalidate();
        repaint();
    }

    /**
     * Initialise files.
     */
    private void setFiles() {
        for (File file : getHistory()) {
            files.put(file.getName(), file);
        }
    }

    /**
     * Gets the file history.
     */
    private File[] getHistory() {
        File folder = new File(FOLDER_HISTORY);
        if (folder.exists()) {
            return folder.listFiles();
        } else {
            folder.mkdirs();
            return null;
        }
    }
}
