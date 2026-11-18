
package gui;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import model.SentenceMatch;
import model.DocumentData;
import model.AnalysisResult;
import service.DocumentReader;
import service.SimilarityService;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.util.regex.*;

public class MainFrame extends JFrame {
        private DefaultTableModel matchTableModel;
    private JTable matchTable;
    private final DocumentReader reader = new DocumentReader();
    private final SimilarityService similarity = new SimilarityService();

    private DocumentData docA, docB;

    private JTextPane previewA = new JTextPane();
    private JTextPane previewB = new JTextPane();

    private JLabel fileA = new JLabel("No document selected");
    private JLabel fileB = new JLabel("No document selected");
    private JLabel status = new JLabel("Upload two documents to begin");
    private JLabel average = new JLabel("Average Similarity: --");

    private JLabel[] metricValues = new JLabel[6];

    private JSlider threshold = new JSlider(0, 100, 30);
    private JLabel thresholdLabel = new JLabel("30%");

    private final String[] metricNames = {
        "Jaccard Similarity",
        "Cosine Similarity",
        "TF-IDF Cosine",
        "Bigram Jaccard",
        "Exact Sentence Matches",
        "Word Counts (A / B)"
    };

    private final Color DARK = new Color(25, 35, 55);
    private final Color BLUE = new Color(55, 110, 220);
    private final Color GREEN = new Color(35, 150, 95);
    private final Color ORANGE = new Color(235, 145, 35);

    public MainFrame() {

        setTitle("PlagiaCheck - Document Similarity Analyzer");
        setSize(1250, 850);
        setMinimumSize(new Dimension(950, 700));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        getContentPane().setBackground(new Color(242, 245, 250));
        setLayout(new BorderLayout(12, 12));

        add(createHeader(), BorderLayout.NORTH);
        add(createMainPanel(), BorderLayout.CENTER);
        add(createFooter(), BorderLayout.SOUTH);

        updatePreview(previewA, "");
        updatePreview(previewB, "");
    }

    private JPanel createHeader() {

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(DARK);
        header.setBorder(new EmptyBorder(18, 25, 18, 25));

        JLabel title = new JLabel("PlagiaCheck");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Arial", Font.BOLD, 27));

        JLabel subtitle = new JLabel(
            "Intelligent Document Similarity Detection System"
        );
        subtitle.setForeground(new Color(200, 210, 230));
        subtitle.setFont(new Font("Arial", Font.PLAIN, 13));

        JPanel text = new JPanel(new GridLayout(2, 1, 0, 5));
        text.setOpaque(false);
        text.add(title);
        text.add(subtitle);

        header.add(text, BorderLayout.WEST);
        return header;
    }

   
private JPanel createMainPanel() {

    JPanel main = new JPanel(new BorderLayout(12, 12));
    main.setOpaque(false);
    main.setBorder(new EmptyBorder(5, 15, 5, 15));

    JPanel documentPanel = createDocumentPanel();
    JPanel resultsPanel = createResultsPanel();

    JSplitPane splitPane = new JSplitPane(
        JSplitPane.VERTICAL_SPLIT,
        documentPanel,
        resultsPanel
    );

    splitPane.setResizeWeight(0.48);
    splitPane.setDividerSize(8);
    splitPane.setContinuousLayout(true);
    splitPane.setOneTouchExpandable(true);
    splitPane.setBorder(null);

    splitPane.setDividerLocation(0.48);

    main.add(splitPane, BorderLayout.CENTER);

    return main;


    }

    private JPanel createDocumentPanel() {

        JPanel panel = new JPanel(new GridLayout(1, 2, 15, 0));
        panel.setOpaque(false);

        panel.add(createDocumentCard(
            "DOCUMENT A", previewA, fileA, true
        ));

        panel.add(createDocumentCard(
            "DOCUMENT B", previewB, fileB, false
        ));

        return panel;
    }

    private JPanel createDocumentCard(
        String heading,
        JTextPane preview,
        JLabel filename,
        boolean first
    ) {

        JPanel card = new JPanel(new BorderLayout(8, 8));
        card.setBackground(Color.WHITE);
        card.setBorder(new CompoundBorder(
            new LineBorder(new Color(220, 225, 235), 1, true),
            new EmptyBorder(12, 12, 12, 12)
        ));

        JLabel title = new JLabel(heading);
        title.setFont(new Font("Arial", Font.BOLD, 16));
        title.setForeground(DARK);

        JButton upload = new JButton("Choose File");
        upload.setBackground(BLUE);
        upload.setForeground(Color.WHITE);
        upload.setFocusPainted(false);

        upload.addActionListener(e -> chooseDocument(first));

   JPanel top = new JPanel(new BorderLayout());
        top.add(title, BorderLayout.WEST);
        top.add(upload, BorderLayout.EAST);

        filename.setFont(new Font("Arial", Font.PLAIN, 11));
        filename.setForeground(Color.GRAY);

        JPanel headingPanel = new JPanel(new BorderLayout(5, 8));
        headingPanel.setOpaque(false);
        headingPanel.add(top, BorderLayout.NORTH);
        headingPanel.add(filename, BorderLayout.SOUTH);

        preview.setEditable(false);
        preview.setFont(new Font("Arial", Font.PLAIN, 13));
        preview.setBackground(new Color(250, 251, 253));
        preview.setMargin(new Insets(10, 10, 10, 10));

        JScrollPane scroll = new JScrollPane(preview);
        scroll.setBorder(new LineBorder(new Color(225, 230, 238)));

        JLabel legend = new JLabel(
            "<html><font color='#23965F'>■ Common words</font> &nbsp; "
            + "<font color='#EB9123'>■ Unique words</font></html>"
        );
        legend.setFont(new Font("Arial", Font.PLAIN, 12));

        card.add(headingPanel, BorderLayout.NORTH);
        card.add(scroll, BorderLayout.CENTER);
        card.add(legend, BorderLayout.SOUTH);

        return card;
    }

    private JPanel createResultsPanel() {

        JPanel panel = new JPanel(new BorderLayout(8, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new CompoundBorder(
            new LineBorder(new Color(220, 225, 235), 1, true),
            new EmptyBorder(12, 15, 12, 15)
        ));

        JLabel heading = new JLabel("SIMILARITY ANALYSIS RESULTS");
        heading.setFont(new Font("Arial", Font.BOLD, 16));
        heading.setForeground(DARK);

        JPanel cards = new JPanel(new GridLayout(2, 3, 10, 10));
        cards.setOpaque(false);

        for (int i = 0; i < 6; i++) {

            JPanel card = new JPanel(new GridLayout(2, 1, 2, 5));
            card.setBackground(new Color(246, 248, 252));
            card.setBorder(new CompoundBorder(
                new LineBorder(new Color(225, 230, 238)),
                new EmptyBorder(9, 10, 9, 10)
            ));

            JLabel name = new JLabel(metricNames[i]);
            name.setFont(new Font("Arial", Font.PLAIN, 12));
            name.setForeground(Color.DARK_GRAY);

            metricValues[i] = new JLabel("--");
            metricValues[i].setFont(new Font("Arial", Font.BOLD, 19));
            metricValues[i].setForeground(BLUE);

            card.add(name);
            card.add(metricValues[i]);

            cards.add(card);
        }

// Sentence matching results table
String[] columns = {
    "Document A",
    "Document B",
    "Similarity",
    "Match Type"
};

matchTableModel = new DefaultTableModel(columns, 0) {
    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }
};

matchTable = new JTable(matchTableModel);
matchTable.setRowHeight(42);
matchTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
matchTable.setFillsViewportHeight(true);
matchTable.setFont(new Font("Arial", Font.PLAIN, 12));
matchTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));

// Wrap long sentences instead of cutting them off.
WrappedTextRenderer wrappedRenderer = new WrappedTextRenderer();
matchTable.getColumnModel().getColumn(0).setCellRenderer(wrappedRenderer);
matchTable.getColumnModel().getColumn(1).setCellRenderer(wrappedRenderer);

// Give the sentence columns more room than the score/type columns.
matchTable.getColumnModel().getColumn(0).setPreferredWidth(430);
matchTable.getColumnModel().getColumn(1).setPreferredWidth(430);
matchTable.getColumnModel().getColumn(2).setPreferredWidth(100);
matchTable.getColumnModel().getColumn(3).setPreferredWidth(100);

JScrollPane matchScroll = new JScrollPane(matchTable);
matchScroll.setPreferredSize(new Dimension(900, 165));

JPanel matchPanel = new JPanel(new BorderLayout(5, 5));
matchPanel.setOpaque(false);

JLabel matchHeading = new JLabel("SENTENCE MATCHING DETAILS");
matchHeading.setFont(new Font("Arial", Font.BOLD, 14));

matchPanel.add(matchHeading, BorderLayout.NORTH);
matchPanel.add(matchScroll, BorderLayout.CENTER);

        JPanel bottom = new JPanel(new BorderLayout(10, 8));
        bottom.setOpaque(false);

        average.setFont(new Font("Arial", Font.BOLD, 14));
        average.setForeground(DARK);

        JPanel sliderPanel = new JPanel(new BorderLayout(5, 2));
        sliderPanel.setOpaque(false);

        JLabel thresholdText = new JLabel("Alert Threshold:");
        thresholdText.setFont(new Font("Arial", Font.PLAIN, 12));

        threshold.setMajorTickSpacing(25);
        threshold.setPaintTicks(true);
        threshold.setPreferredSize(new Dimension(180, 35));

        threshold.addChangeListener(e -> {
            thresholdLabel.setText(threshold.getValue() + "%");
        });

        JPanel sliderLine = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        sliderLine.setOpaque(false);
        sliderLine.add(threshold);
        sliderLine.add(thresholdLabel);

        sliderPanel.add(thresholdText, BorderLayout.NORTH);
        sliderPanel.add(sliderLine, BorderLayout.CENTER);

        bottom.add(average, BorderLayout.WEST);
        bottom.add(sliderPanel, BorderLayout.EAST);

        JPanel centerContent = new JPanel();
        centerContent.setLayout(new BoxLayout(centerContent, BoxLayout.Y_AXIS));
        centerContent.setOpaque(false);
        centerContent.add(cards);
        centerContent.add(Box.createVerticalStrut(12));
        centerContent.add(matchPanel);

        JPanel content = new JPanel(new BorderLayout(5, 10));
        content.setOpaque(false);
        content.add(heading, BorderLayout.NORTH);
        content.add(centerContent, BorderLayout.CENTER);
        content.add(bottom, BorderLayout.SOUTH);

        panel.add(content, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createFooter() {

        JPanel footer = new JPanel(new BorderLayout(10, 5));
        footer.setBackground(new Color(242, 245, 250));
        footer.setBorder(new EmptyBorder(5, 15, 12, 15));

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 5));
        buttons.setOpaque(false);

        JButton analyze = new JButton("Analyze Documents");
        JButton clear = new JButton("Clear");
        JButton export = new JButton("Export Report");

        analyze.setBackground(BLUE);
        analyze.setForeground(Color.WHITE);

        export.setBackground(GREEN);
        export.setForeground(Color.WHITE);

        analyze.addActionListener(e -> analyzeDocuments());
        clear.addActionListener(e -> clearAll());
        export.addActionListener(e -> exportReport());

        buttons.add(analyze);
        buttons.add(clear);
        buttons.add(export);

        status.setHorizontalAlignment(SwingConstants.CENTER);
        status.setFont(new Font("Arial", Font.PLAIN, 12));

        footer.add(buttons, BorderLayout.NORTH);
        footer.add(status, BorderLayout.SOUTH);

        return footer;
    }

    private void chooseDocument(boolean first) {

        JFileChooser chooser = new JFileChooser();

        int result = chooser.showOpenDialog(this);

        if (result != JFileChooser.APPROVE_OPTION) {
            return;
        }

        File selected = chooser.getSelectedFile();

        try {

            DocumentData document = reader.read(selected);

            if (first) {
                docA = document;
                fileA.setText(selected.getName());
                updatePreview(previewA, document.getContent());
            } else {
                docB = document;
                fileB.setText(selected.getName());
                updatePreview(previewB, document.getContent());
            }

            if (docA != null && docB != null) {
                highlightDocuments();
            }

            status.setText("Document uploaded successfully.");

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                this,
                ex.getMessage(),
                "File Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void analyzeDocuments() {

        if (docA == null || docB == null) {

            JOptionPane.showMessageDialog(
                this,
                "Please select both documents first."
            );
            return;
        }

        try {

            AnalysisResult result = similarity.analyze(docA, docB);

            // Show sentence-level matches in the table
            matchTableModel.setRowCount(0);
            for (SentenceMatch match : result.getSentenceMatches()) {
                matchTableModel.addRow(new Object[] {
                    match.getSentenceA(),
                    match.getSentenceB(),
                    String.format(Locale.US, "%.2f%%", match.getSimilarity()),
                    match.getType()
                });
            }
            updateMatchTableRowHeights();

            Map<String, Double> scores = result.getScores();

            for (int i = 0; i < 4; i++) {

                String key = findScoreKey(scores, i);

                if (key != null) {
                    metricValues[i].setText(
                        String.format(Locale.US, "%.2f%%", scores.get(key))
                    );
                } else {
                    metricValues[i].setText("N/A");
                }
            }

            metricValues[4].setText(
                String.valueOf(result.getMatches().size())
            );

            metricValues[5].setText(
                result.getWordsA() + " / " + result.getWordsB()
            );

            double avg = result.getAverage();

            average.setText(String.format(
                Locale.US,
                "Average Similarity: %.2f%%",
                avg
            ));

            if (avg >= threshold.getValue()) {

                status.setForeground(new Color(190, 65, 50));
                status.setText("Alert: Similarity is above the selected threshold.");

            } else {

                status.setForeground(GREEN);
                status.setText("Analysis completed. Similarity is below threshold.");
            }

            highlightDocuments();

        } catch (Exception ex) {

            JOptionPane.showMessageDialog(
                this,
                "Analysis failed: " + ex.getMessage(),
                "Analysis Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private String findScoreKey(Map<String, Double> scores, int type) {

        for (String key : scores.keySet()) {

            String k = key.toLowerCase(Locale.ROOT);

            if (type == 0 && k.contains("jaccard")
                    && !k.contains("bigram")) return key;

            if (type == 1 && k.contains("cosine")
                    && !k.contains("tf") && !k.contains("idf")) return key;

            if (type == 2 && (k.contains("tf") || k.contains("idf"))) return key;

            if (type == 3 && k.contains("bigram")) return key;
        }

        return null;
    }

    private void highlightDocuments() {

        if (docA == null || docB == null) return;

        Set<String> wordsA = extractWords(docA.getContent());
        Set<String> wordsB = extractWords(docB.getContent());

        Set<String> common = new HashSet<>(wordsA);
        common.retainAll(wordsB);

        applyHighlight(previewA, common);
        applyHighlight(previewB, common);
    }

    private Set<String> extractWords(String text) {

        Set<String> words = new HashSet<>();

        Matcher matcher = Pattern.compile(
            "[\\p{L}\\p{N}']+"
        ).matcher(text.toLowerCase(Locale.ROOT));

        while (matcher.find()) {
            words.add(matcher.group());
        }

        return words;
    }

    private void applyHighlight(JTextPane pane, Set<String> common) {

        String text = pane.getText();

        StyledDocument document = pane.getStyledDocument();

        SimpleAttributeSet normal = new SimpleAttributeSet();
        StyleConstants.setForeground(normal, Color.DARK_GRAY);
        StyleConstants.setBackground(normal, Color.WHITE);

        document.setCharacterAttributes(
            0, document.getLength(), normal, true
        );

        Matcher matcher = Pattern.compile(
            "[\\p{L}\\p{N}']+"
        ).matcher(text);

        SimpleAttributeSet shared = new SimpleAttributeSet();
        StyleConstants.setBackground(shared, new Color(190, 240, 205));
        StyleConstants.setForeground(shared, new Color(20, 100, 55));

        SimpleAttributeSet unique = new SimpleAttributeSet();
        StyleConstants.setBackground(unique, new Color(255, 225, 175));
        StyleConstants.setForeground(unique, new Color(150, 85, 10));

        while (matcher.find()) {

            String word = matcher.group().toLowerCase(Locale.ROOT);

            document.setCharacterAttributes(
                matcher.start(),
                matcher.end() - matcher.start(),
                common.contains(word) ? shared : unique,
                true
            );
        }
    }

    // Adjust each row so wrapped sentences are fully visible.
    private void updateMatchTableRowHeights() {
        if (matchTable == null) return;

        for (int row = 0; row < matchTable.getRowCount(); row++) {
            int rowHeight = 42;

            for (int column = 0; column < matchTable.getColumnCount(); column++) {
                if (column == 0 || column == 1) {
                    TableCellRenderer renderer = matchTable.getCellRenderer(row, column);
                    Component component = matchTable.prepareRenderer(renderer, row, column);
                    int preferredHeight = component.getPreferredSize().height + 10;
                    rowHeight = Math.max(rowHeight, preferredHeight);
                }
            }

            matchTable.setRowHeight(row, rowHeight);
        }
    }

    // Renderer that wraps long sentence text onto multiple lines.
    private static class WrappedTextRenderer extends JTextArea
            implements TableCellRenderer {

        public WrappedTextRenderer() {
            setLineWrap(true);
            setWrapStyleWord(true);
            setOpaque(true);
            setBorder(new EmptyBorder(5, 5, 5, 5));
        }

        @Override
        public Component getTableCellRendererComponent(
                JTable table, Object value, boolean isSelected,
                boolean hasFocus, int row, int column) {

            setText(value == null ? "" : value.toString());

            if (isSelected) {
                setBackground(table.getSelectionBackground());
                setForeground(table.getSelectionForeground());
            } else {
                setBackground(Color.WHITE);
                setForeground(Color.DARK_GRAY);
            }

            setSize(table.getColumnModel().getColumn(column).getWidth(), Short.MAX_VALUE);
            return this;
        }
    }

    private void updatePreview(JTextPane pane, String text) {
        pane.setText(text);
        pane.setCaretPosition(0);
    }

    private void clearAll() {

        docA = null;
        docB = null;
        if (matchTableModel != null) {
            matchTableModel.setRowCount(0);
        }

        fileA.setText("No document selected");
        fileB.setText("No document selected");

        previewA.setText("");
        previewB.setText("");

        for (JLabel value : metricValues) {
            value.setText("--");
        }

        average.setText("Average Similarity: --");
        status.setForeground(Color.DARK_GRAY);
        status.setText("Upload two documents to begin");
    }

    private void exportReport() {

        if (docA == null || docB == null
                || metricValues[0].getText().equals("--")) {

            JOptionPane.showMessageDialog(
                this,
                "Analyze two documents before exporting."
            );
            return;
        }

        JFileChooser chooser = new JFileChooser();
        chooser.setSelectedFile(new File("PlagiaCheck_Report.txt"));

        if (chooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) {
            return;
        }

        File output = chooser.getSelectedFile();

        try (PrintWriter writer = new PrintWriter(
                new FileWriter(output))) {

            writer.println("PLAGIACHECK ANALYSIS REPORT");
            writer.println("==========================");
            writer.println();

            writer.println("Document A: " + docA.getName());
            writer.println("Document B: " + docB.getName());
            writer.println();

            for (int i = 0; i < 6; i++) {
                writer.println(
                    metricNames[i] + ": " + metricValues[i].getText()
                );
            }

            writer.println();
            writer.println(average.getText());
            writer.println();
            writer.println("Threshold: " + threshold.getValue() + "%");
            writer.println();
            writer.println("SENTENCE MATCHING DETAILS");
            if (matchTableModel != null) {
                for (int row = 0; row < matchTableModel.getRowCount(); row++) {
                    writer.println("A: " + matchTableModel.getValueAt(row, 0));
                    writer.println("B: " + matchTableModel.getValueAt(row, 1));
                    writer.println("Similarity: " + matchTableModel.getValueAt(row, 2));
                    writer.println("Type: " + matchTableModel.getValueAt(row, 3));
                    writer.println();
                }
            }

            JOptionPane.showMessageDialog(
                this,
                "Report exported successfully!"
            );

        } catch (IOException ex) {

            JOptionPane.showMessageDialog(
                this,
                "Could not export report: " + ex.getMessage()
            );
        }
    }
}
