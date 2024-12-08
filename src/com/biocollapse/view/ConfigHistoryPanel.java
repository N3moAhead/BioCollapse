// Authors: Johann
package src.com.biocollapse.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
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

        add(savePanel(), BorderLayout.NORTH);
        loadFiles();

        JPanel options = new JPanel();

        load = new JButton("Laden");
        load.setEnabled(false);
        load.addActionListener(e -> {
            File file = files.get((String) list.getSelectedValue());
            try (FileInputStream fileIn = new FileInputStream(file); ObjectInputStream objectIn = new ObjectInputStream(fileIn)) {
                GlobalConfig.setConfig((Config) objectIn.readObject());
                configPanel.revalidateConfig();
            } catch (Exception ignored) {
                JOptionPane.showMessageDialog(configPanel, "Fehler bei dem Laden.", "Laden fehlgeschlagen", JOptionPane.ERROR_MESSAGE);
            }
        });
        options.add(load);

        delete = new JButton("Löschen");
        delete.setEnabled(false);
        delete.addActionListener(e -> {
            File file = files.get((String) list.getSelectedValue());
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
     * Save current config panel.
     */
    private JPanel savePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        JTextField field = new JTextField(10);
        field.setToolTipText("Name");
        JButton btn = new JButton("Speichern");
        btn.addActionListener(e -> saveConfig(field));
        panel.add(field);
        panel.add(btn);
        panel.setBorder(new TitledBorder("Aktueller Stand"));
        return panel;
    }

    private void saveConfig(JTextField field) {
        int maxMaps = 20;
        if (files.size()>=maxMaps) {
            JOptionPane.showMessageDialog(configPanel, "Fehler bei der Speicherung. Es können maximal "+maxMaps+" gespeichert sein. Bitte vorher andere Speicherungen löschen.", "Speicherung fehlgeschlagen", JOptionPane.ERROR_MESSAGE);
        }
        String name = field.getText();
        if (name.length()==0) {
            JOptionPane.showMessageDialog(configPanel, "Fehler bei der Speicherung. Bitte einen Name angeben.", "Speicherung fehlgeschlagen", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (files.containsKey(name)) {
            JOptionPane.showMessageDialog(configPanel, "Fehler bei der Speicherung. Dateiname bereits vorhanden.", "Speicherung fehlgeschlagen", JOptionPane.ERROR_MESSAGE);
            return;
        }

        configPanel.setConfig();

        if (saveConfigAsObject(name)) {
            JOptionPane.showMessageDialog(configPanel, "Zustand gespeichert als " + name, "Speicherung erfolgreich", JOptionPane.INFORMATION_MESSAGE);
            field.setText("");
        } else {
            JOptionPane.showMessageDialog(configPanel, "Fehler bei der Speicherung. Bitte erneut versuchen.", "Speicherung fehlgeschlagen", JOptionPane.ERROR_MESSAGE);
        }
        loadFiles();
    }

    /**
     * Store the config object as a file.
     */
    private boolean saveConfigAsObject(String name) {
        try (FileOutputStream fileOut = new FileOutputStream(ConfigHistoryPanel.FOLDER_HISTORY+"/"+name); ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
            out.writeObject(GlobalConfig.getConfig());
            return true;
        } catch (Exception ignored) {
            return false;
        }
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
        list.setBorder(new TitledBorder("Lokal"));
        if (files.keySet().isEmpty()) {
            add(new JLabel("Noch nichts gespeichert.", SwingConstants.CENTER));
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
