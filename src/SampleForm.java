import com.intellij.openapi.diff.impl.dir.actions.popup.SetDefault;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by haegyun on 11/7/15.
 */
public class SampleForm extends JFrame {
    private JButton clickMeButton;
    private JPanel rootPanel;

    public SampleForm() {
        super("Hello World!");

        setContentPane(rootPanel);

        pack();
//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        clickMeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showConfirmDialog(SampleForm.this, "You clicked the button!");
            }
        });

        setVisible(true);
    }
}
