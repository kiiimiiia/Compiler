package com.company;
//

// Java program to create a blank text
// field of definite number of columns.
// Java program to create a blank text field and set BOLD font type
import java.awt.event.*;
import java.awt.*;
import javax.swing.*;
class text extends JFrame implements ActionListener {
    // JTextField
    static JTextField t;

    // JFrame
    static JFrame f;

    // JButton
    static JButton b;

    // label to diaplay text
    static JLabel l;

    String Filename = "!" ;

    // default constructor
    text()
    {

        f = new JFrame("ChooseFile");

        // create a label to display text
        l = new JLabel("nothing entered");

        // create a new button
        b = new JButton("submit");
        b.addActionListener(this);
        t = new JTextField(16);
        Font fo = new Font("Serif", Font.BOLD, 20);
        t.setFont(fo);
        JPanel p = new JPanel();
        p.add(t);
        p.add(b);
        p.add(l);
        f.add(p);
        f.setSize(300, 300);

        f.show();
    }

    public void actionPerformed(ActionEvent e)
    {
        String s = e.getActionCommand();
        if (s.equals("submit")) {
            // set the text of the label to the text of the field
            l.setText(t.getText());
            Filename = t.getText();

            // set the text of field to blank
            t.setText("  ");
        }
    }
    public String getFilename(){
        return Filename;
    }


}




////This program shows a series of dialog boxes one
////after the other
////Imports are listed in full to show what's being used
////could just import javax.swing.* and java.awt.* etc..
//import javax.swing.JFrame;
//        import javax.swing.JOptionPane;
//        import javax.swing.UIManager;
//        import javax.swing.Icon;
//        import java.awt.EventQueue;
//public class SimpleDialogFrame extends JFrame{
//    //Using a standard Java icon
//    private Icon optionIcon = UIManager.getIcon("FileView.computerIcon");
//    //Application start point
//    public static void main(String[] args) {
////Use the event dispatch thread for Swing components
//        EventQueue.invokeLater(new Runnable()
//        {
//            public void run()
//            {
////create GUI frame
//                new SimpleDialogFrame().setVisible(true);
//            }
//        });
//    }
//    public SimpleDialogFrame()
//    {
////make sure the program exits when the frame closes
//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        setTitle("Choose File");
//        setSize(500,500);
////This will center the JFrame in the middle of the screen
//        setLocationRelativeTo(null);
////TO TRY: Comment out the above line and use null for the parent
////component in one of the JOptionPane calls to see the difference
////it makes to the position of the dialog box.
//        setVisible(true);
////Use the showMessageDialog method for a plain message dialog box
//        JOptionPane.showMessageDialog(this, "This is the dialog message"
//                ,"This is the dialog title", JOptionPane.PLAIN_MESSAGE);
////Use the showMessageDialog method for a error message dialog box
//        JOptionPane.showMessageDialog(this, "This is the dialog message"
//                ,"This is the dialog title", JOptionPane.ERROR_MESSAGE);
////Use the showConfirmDialog method for a warning message dialog box
////with OK, CANCEL buttons. Capture the button number with an int variable
//        int choice = JOptionPane.showConfirmDialog(this, "This is the dialog message"
//                ,"This is the dialog title", JOptionPane.WARNING_MESSAGE
//                , JOptionPane.OK_CANCEL_OPTION);
////Use the showConfirmDialog method for an information message dialog box
////with YES, NO, CANCEL buttons. It shows the button choice of previous
////message box
//        JOptionPane.showConfirmDialog(this,"Last button pressed was number " + choice
//                , "This is the dialog title", JOptionPane.INFORMATION_MESSAGE
//                , JOptionPane.YES_NO_CANCEL_OPTION);
////The showOptionDialog method can be made to work as if it were the confirmDialog
////method by using null for the last three parameters. In this case the options for
////the button types (YES, NO, CANCEL) and the message type (INFORMATION_MESSAGE)
////will be used.
//        JOptionPane.showOptionDialog(this, "This is the dialog message"
//                , "This is the dialog title", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE
//                ,null, null, null);
////Use the showOptionDialog method to make a custom box. If the options parameter
////is null the YES, NO, CANCEL buttons are used. Also notice that even though
////the message type is INFORMATION_MESSAGE the usual icon is overriden by the one
////provided.
//        JOptionPane.showOptionDialog(this, "This is the dialog message"
//                , "This is the dialog title", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE
//                ,optionIcon, null, null);
////String array to be used for the buttons
//        String[] buttonOptions = new String[] {"Happy Button", "Sad Button", "Confused Button"};
////If the options parameter is not null the YES, NO, CANCEL buttons are not used
////The buttons are made with the object array - in this case a String array.
//        JOptionPane.showOptionDialog(this, "This is the dialog message"
//                , "This is the dialog title", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE
//                ,optionIcon, buttonOptions, buttonOptions[0]);
//    }
//}