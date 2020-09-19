
package java_notepad;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.plaf.basic.BasicSliderUI;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;


public class Java_Notepad extends JFrame {
   static JTextArea mainarea;
   
    JMenuBar mbar;
    JMenu mnuFile,mnuEdit,MnuFormat,mnuHelp;
    JMenuItem itmNew,itmOpen,itmSave,itmSaveAs,itmExit,itmCut,itmCopy,itmPaste,itmFontColor,itmFind,itmReplace,itmFont;
  
    JCheckBoxMenuItem wordWrap;
    String filename;
    JFileChooser jc;
    String FileContent;
    UndoManager undo;
    UndoAction undoAction;
    RedoAction redoAction;
    String findText;
    int fnext=1;
   // public static Java_Notepad frmMain=new Java_Notepad();
    FontHelper font;
    JToolBar toolbar;
    

    public Java_Notepad()  {
         initComponents();
         itmSave.addActionListener(new ActionListener() {
             @Override
             public void actionPerformed(ActionEvent e) {
                 try {
                     Save();
                 } catch (IOException ex) {
                     Logger.getLogger(Java_Notepad.class.getName()).log(Level.SEVERE, null, ex);
                 }
             }
         });
         itmSaveAs.addActionListener(new ActionListener(){
               @Override
             public void actionPerformed(ActionEvent e) {
          SaveAs();
             }
         });
         itmOpen.addActionListener(new ActionListener(){
               @Override
             public void actionPerformed(ActionEvent e) {
          open();
             }
         });
          itmNew.addActionListener(new ActionListener(){
               @Override
             public void actionPerformed(ActionEvent e) {
                   try {
                       open_new();
                   } catch (IOException ex) {
                       Logger.getLogger(Java_Notepad.class.getName()).log(Level.SEVERE, null, ex);
                   }
             }
         });
           itmOpen.addActionListener(new ActionListener(){
               @Override
             public void actionPerformed(ActionEvent e) {
          open();
             }
         }); itmExit.addActionListener(new ActionListener(){
               @Override
             public void actionPerformed(ActionEvent e) {
          System.exit(0);
             }
         });
         itmCut.addActionListener(new ActionListener(){
               @Override
             public void actionPerformed(ActionEvent e) {
          mainarea.cut();
             }
         });
         itmCopy.addActionListener(new ActionListener(){
               @Override
             public void actionPerformed(ActionEvent e) {
          mainarea.copy();
             }
         });
         itmPaste.addActionListener(new ActionListener(){
               @Override
             public void actionPerformed(ActionEvent e) {
          mainarea.paste();
             }
         });
         mainarea.getDocument().addUndoableEditListener(new UndoableEditListener(){
              @Override
             public void undoableEditHappened(UndoableEditEvent e) {
                    undo.addEdit(e.getEdit());
                      undoAction.update();
                      redoAction.update();
             }
         });
         
        // mainarea.setWrapStyleWord(true);
         wordWrap.addActionListener(new ActionListener(){
               @Override
             public void actionPerformed(ActionEvent e) {
          if(wordWrap.isSelected()){
              mainarea.setLineWrap(true);
          mainarea.setWrapStyleWord(true);
          }
          else{
              mainarea.setLineWrap(false);
           mainarea.setWrapStyleWord(false);
          }
             }
         });
         itmFontColor.addActionListener(new ActionListener(){
               @Override
             public void actionPerformed(ActionEvent e) {
          Color c=JColorChooser.showDialog(rootPane," Choose Font Color", Color.yellow);
          mainarea.setForeground(c);
             }
         });
         itmFind.addActionListener(new ActionListener(){
               @Override
             public void actionPerformed(ActionEvent e) {
             new FindAndReplace(null,false);
             }
         });
          itmReplace.addActionListener(new ActionListener(){
               @Override
             public void actionPerformed(ActionEvent e) {
             new FindAndReplace(null,true);
             }
         });
           itmFont.addActionListener(new ActionListener(){
               @Override
             public void actionPerformed(ActionEvent e) {
             font.setVisible(true);
             }
         });
            font.getOk().addActionListener(new ActionListener(){
               @Override
             public void actionPerformed(ActionEvent e) {
            mainarea.setFont(font.font());
             font.setVisible(false);
             }
         });
             font.getCancel().addActionListener(new ActionListener(){
               @Override
             public void actionPerformed(ActionEvent e) {
             font.setVisible(false);
             }
         });
    }
         
         
             
         
    
    private void initComponents(){
        jc= new JFileChooser(".");
        mainarea = new JTextArea();
        undo=new UndoManager();
        font=new FontHelper();
       
        undoAction= new UndoAction();
        redoAction= new RedoAction();
        getContentPane().add(mainarea);
        getContentPane().add(new JScrollPane(mainarea), BorderLayout.CENTER);
        
        setTitle("Untitled notepad");
        setSize(800, 600);
        //menu bar
        mbar = new JMenuBar();
        //menu
        mnuFile = new JMenu("File");
        mnuEdit = new JMenu("Edit");
        MnuFormat = new JMenu("Format");
        mnuHelp = new JMenu("Help");
        //menu item
        itmNew= new JMenuItem("New");
        itmOpen= new JMenuItem("Open");
        itmSave= new JMenuItem("Save");
        itmSaveAs= new JMenuItem("Save As");
        itmExit=new JMenuItem("Exit");
        itmCut=new JMenuItem("Cut");
        itmCopy=new JMenuItem("Copy");
      itmPaste  =new JMenuItem("Paste");
      itmFind  =new JMenuItem("Find");
      itmReplace  =new JMenuItem("Replace");
      
          itmFontColor  =new JMenuItem("Font Color");
           itmFont  =new JMenuItem(" Font ");
      wordWrap = new JCheckBoxMenuItem("Word Wrap");
  
        
        
        //adding shortcut
        itmNew.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N,ActionEvent.CTRL_MASK));
         itmOpen.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O,ActionEvent.CTRL_MASK));
          itmSave.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,ActionEvent.CTRL_MASK));
           itmExit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4,ActionEvent.ALT_MASK));
           itmCut.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X,ActionEvent.CTRL_MASK));
           itmCopy.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C,ActionEvent.CTRL_MASK));
           itmPaste.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V,ActionEvent.CTRL_MASK));
          
        //add menu item
        mnuFile.add(itmNew);
        mnuFile.add(itmOpen);
        mnuFile.add(itmSave);
        mnuFile.add(itmSaveAs);
        mnuFile.addSeparator();
        mnuFile.add(itmExit);
        mnuEdit.add(undoAction);
        mnuEdit.add(redoAction);
        mnuEdit.add(itmCut);
        mnuEdit.add(itmCopy);
        mnuEdit.add(itmPaste);
         mnuEdit.add(itmFind);
          mnuEdit.add(itmReplace);
           mnuEdit.add(itmFont);
        MnuFormat.add(wordWrap);
         MnuFormat.add(itmFontColor);
        
        //add menu item to menu bar
        mbar.add(mnuFile);
         mbar.add(mnuEdit);
          mbar.add(MnuFormat);
           mbar.add(mnuHelp);
           //add menu bar to frame
           setJMenuBar(mbar);
        
        
        
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        
        
    }
    private void Save() throws IOException{
        PrintWriter fout= null;
       // int retval=-1;
        try 
        {
            if(filename==null){
                SaveAs();
           
        }
        
        else{
         fout= new PrintWriter(new FileWriter(filename));   
                String s= mainarea.getText();
            StringTokenizer st = new StringTokenizer(s,System.getProperty("Line separator"));
            while (st.hasMoreElements()) {
                fout.println(st.nextToken());
            }
            JOptionPane.showMessageDialog(rootPane,"File Saved");
            FileContent=mainarea.getText();
                }
        }
     catch (IOException e) {
        }
        
        finally{
            if(fout!=null)
            fout.close();
                } 
        
    }
    private void SaveAs(){
        PrintWriter fout= null;
        int retval=-1;
        try {
             retval=jc.showSaveDialog(this);
            if (retval==JFileChooser.APPROVE_OPTION){
                 
          fout= new PrintWriter(new FileWriter(jc.getSelectedFile()));
            }
            File file=jc.getSelectedFile();
         if(file.exists()){
        int  option= JOptionPane.showConfirmDialog(rootPane,"DO you want to replace this file?", "Confirmation",JOptionPane.OK_CANCEL_OPTION);
         if(option==1){
          String s= mainarea.getText();
            StringTokenizer st = new StringTokenizer(s,System.getProperty("Line separator"));
            while (st.hasMoreElements()) {
                fout.println(st.nextToken());
            }
            JOptionPane.showMessageDialog(rootPane,"File Saved");
            FileContent=mainarea.getText();
            filename= jc.getSelectedFile().getName();
            setTitle(filename= jc.getSelectedFile().getName());
         }
         }
         else{
          String s= mainarea.getText();
            StringTokenizer st = new StringTokenizer(s,System.getProperty("Line separator"));
            while (st.hasMoreElements()) {
                fout.println(st.nextToken());
            }
            JOptionPane.showMessageDialog(rootPane,"File Saved");
            FileContent=mainarea.getText();
            filename= jc.getSelectedFile().getName();
            setTitle(filename= jc.getSelectedFile().getName());
         }
           
            
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            fout.close();
                } 

    }
   private void open(){
       try {
           int retval=jc.showOpenDialog(this);
           if (retval==JFileChooser.APPROVE_OPTION){
           mainarea.setText(null);
           Reader in =new FileReader(jc.getSelectedFile());
           char[]buff=new char[100000];
           int nch;
           while((nch=in.read(buff,0,buff.length))!=-1){
               mainarea.append(new String(buff,0,nch));
                filename= jc.getSelectedFile().getName();
            setTitle(filename= jc.getSelectedFile().getName());
           
           }
           }
       } catch (Exception e) {
           e.printStackTrace();
       }
   }
     private void open_new() throws IOException{
         if(!mainarea.getText().equals("") && mainarea.getText().equals(FileContent)){
     if(filename==null){
         int option=JOptionPane.showConfirmDialog(rootPane, "DO you want to save changes?");
         if(option==0){
         SaveAs();
         clear();
         }
         else{
         clear();
         }
     }
     else{
     int option=JOptionPane.showConfirmDialog(rootPane, "DO you want to save changes?");
     if(option==0){
     Save();
     clear();
     }
     }
         }
         else{
         clear();
         }
     }
     private void clear(){
     mainarea.setText(null);
     setTitle("Untitled Notepad");
     filename=null;
     FileContent= null;
     }
     class UndoAction extends AbstractAction{
         public UndoAction(){
         super("Undo");
         setEnabled(false);
         }
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                undo.undo();
            } catch (CannotUndoException ex) {
                ex.printStackTrace();
            }
            update();
            redoAction.update();
        }
        protected void update(){
        if(undo.canUndo()){
            setEnabled(true);
            putValue(Action.NAME, "Undo");
        }
        }
         
     }
 class RedoAction extends AbstractAction{
         public RedoAction(){
         super("Redo");
         setEnabled(false);
         }
        @Override
        public void actionPerformed(ActionEvent e) {
              try {
                undo.redo();
            } catch (CannotRedoException ex) {
                ex.printStackTrace();
            }
            update();
            undoAction.update();
        }
        protected void update(){
          if(undo.canRedo()){
            setEnabled(true);
            putValue(Action.NAME, "Redo");
        }
        }
         
     }
     
     
    public static void main(String[] args) {
     Java_Notepad jn = new Java_Notepad();   
    }
    public static JTextArea getArea(){
    return mainarea;
    }
}
