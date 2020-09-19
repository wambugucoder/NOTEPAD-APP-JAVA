
package java_notepad;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import static java.awt.SystemColor.text;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.plaf.basic.BasicComboBoxRenderer;

public class FindAndReplace extends JDialog implements ActionListener {
    boolean foundOne,isReplace;
   JTextField searchText, replaceText;
   JCheckBox cbCase, cbWhole;
   JRadioButton up, down;
   JLabel statusinfo;
   JFrame owner;
   JPanel north,center,south;

    public FindAndReplace(JFrame owner,boolean isReplace) {
        super(owner,true);
        this.isReplace=isReplace;
       
        north=new JPanel();
        center=new JPanel();
        south=new JPanel();
         if(isReplace){
        setTitle("Find And Replace");
             setReplacePanel(north);
        }
         else{
            setTitle("Find");
           setFindPanel (north); 
         }
        addComponent(center);
        statusinfo=new JLabel("Status info");
        south.add(statusinfo);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        
        });
        getContentPane().add(north,BorderLayout.NORTH);
        getContentPane().add(center,BorderLayout.CENTER);
        getContentPane().add(south,BorderLayout.SOUTH);
        pack();
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);
        
        setVisible(true);
    }
    private void addComponent(JPanel center){
        JPanel east= new JPanel();
        JPanel west= new JPanel();
        center.setLayout(new GridLayout(1, 2));
        east.setLayout(new GridLayout(2, 1));
    west.setLayout(new GridLayout(2, 1));
    cbCase = new JCheckBox("Match Case",true);
    cbWhole = new JCheckBox("Match Word",true);
    ButtonGroup group=new ButtonGroup();
     up= new JRadioButton("Search up",false);
     down= new JRadioButton("Search down",true);
     group.add(up);
     group.add(down);
     east.add(cbCase);
     east.add(cbWhole);
     east.setBorder(BorderFactory.createTitledBorder("Search Options"));
     west.add(up);
     west.add(down);
       west.setBorder(BorderFactory.createTitledBorder("Search Direction"));
       center.add(east);
        center.add(west);
    }
   private void setFindPanel(JPanel north){
   final JButton NEXT=new JButton("Find Next");
   NEXT.addActionListener(this);
   NEXT.setEnabled(false);
   searchText= new JTextField(20);
   searchText.addActionListener(this);
    searchText.addKeyListener(new KeyAdapter() {
       @Override
       public void keyReleased(KeyEvent e) {
           boolean state=(searchText.getDocument().getLength()>0);
            NEXT.setEnabled(true);
            foundOne= false;
            
           
       }
        
});
    if(searchText.getText().length()>0){
     NEXT.setEnabled(true);
    }
    north.add(new JLabel("Find Word:"));
    north.add(searchText);
     north.add(NEXT);
   }
   private void setReplacePanel(JPanel north){
       GridBagLayout grid=new GridBagLayout();
       north.setLayout(grid);
       GridBagConstraints con= new GridBagConstraints();
       con.fill=GridBagConstraints.HORIZONTAL;
       JLabel lblFword= new JLabel("Find Word :");
       JLabel lblRword= new JLabel("Replace word Word :");
       final JButton NEXT=new JButton("Replace Text");
       NEXT.addActionListener(this);
       NEXT.setEnabled(false);
        final JButton REPLACE=new JButton("Replace All");
       REPLACE.addActionListener(this);
       REPLACE.setEnabled(false);
       searchText=new JTextField(20);
       replaceText=new JTextField(20);
    replaceText.addKeyListener(new KeyAdapter() {
           @Override
           public void keyReleased(KeyEvent e) {
               boolean state=(replaceText.getDocument().getLength()>0);
            NEXT.setEnabled(true);
            REPLACE.setEnabled(true);
            foundOne= false;
           }
        
});
    con.gridx=0;
    con.gridy=0;
    grid.setConstraints(lblFword, con);
    north.add(lblFword);
     con.gridx=1;
    con.gridy=0;
    grid.setConstraints(searchText, con);
    north.add(searchText);
     con.gridx=2;
    con.gridy=0;
    grid.setConstraints(NEXT, con);
    north.add(NEXT);
       con.gridx=0;
    con.gridy=1;
    grid.setConstraints(lblRword, con);
    north.add(lblRword);
     con.gridx=1;
    con.gridy=1;
    grid.setConstraints(replaceText, con);
    north.add(replaceText);
     con.gridx=2;
    con.gridy=1;
    grid.setConstraints(REPLACE, con);
    north.add(REPLACE);
   }
 

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource().equals(searchText)||e.getSource().equals(replaceText)){
        validate();
        }else{
        process();
        if(e.getActionCommand().equals("Replace All")){
        replaceAll();
        }
        }
    }
    private void process(){
    if(isReplace){
    statusinfo.setText("Replacing"+searchText.getText());
    }else{
     statusinfo.setText("Searching for"+searchText.getText());
    }
    int caret=Java_Notepad.getArea().getCaretPosition();
    String word=getWord();
    String Text =getAllText();
    caret=search(Text,word,caret);
    if(caret<0){
        endResult(false,0);
    }else{
    
    }
    }
    private void endResult(boolean isReplace,int tally){
    String message="";
    if(isReplace){
        if(tally==0){
        message=searchText.getText()+"not found";
        }
        else if(tally==1){
        message=" One change was made to"+searchText.getText();
        }
        else{
        message=""+tally+"changes were made";
        }
    
    }else{
    String str="";
    if(isScarchDown()){
    str="Search down";
    }else{
    str="Search up";
    }if(foundOne && !isReplace){
    message="End of"+str+"for"+searchText.getText();
    }
    else{
        if(foundOne && isReplace){
         message="End of Replace"+searchText.getText() +"with"+replaceText.getText();
        }
    
    }
    statusinfo.setText(message);
    }
    }
    
    private int search(String Text,String word,int caret){
    boolean found=false;
    int all=Text.length();
    int check=word.length();
    if(isScarchDown()){
    int add=0;
        for (int i = caret+1; i <(all-check); i++) {
            String temp=Text.substring(i,(i + check));
            if(temp.equals(word)){
                if(wholeWordIsSelected()){
                if(checkForWholeWord(check, Text, add, caret)){
                caret=i;
                found= true;
                break;
                }
                }
                else{//not whole word
                 caret=i; 
                 found= true;
                break;
                
                }
            
            }
        }
    }
    else{
    int add=caret;
        for (int i = caret-1; i >= check; i--) {
            add--;
            String temp=Text.substring((i-check),i);
             if(temp.equals(word)){
                if(wholeWordIsSelected()){
                if(checkForWholeWord(check, Text, add, caret)){
                caret=i;
                found= true;
                break;
                }
                }
                else{//not whole word
                 caret=i; 
                 found= true;
                break;
                
                }
            
            }
        }
    }
    Java_Notepad.getArea().setCaretPosition(0);
    if(found){
        Java_Notepad.getArea().requestFocus();
        if(isScarchDown()){
            Java_Notepad.getArea().select(caret,caret+ check);
        }else{
         Java_Notepad.getArea().select(caret-check,caret);
        }
        foundOne=true;
        return caret;
    }

            return -1;
    }
    private String getAllText(){
    if(caseNotSelected()){
    return Java_Notepad.getArea().getText().toLowerCase();
    }else{
    return Java_Notepad.getArea().getText();
    }
    }
    private String getWord(){
    if(caseNotSelected()){
    return Java_Notepad.getArea().getText().toLowerCase();
    }
    return Java_Notepad.getArea().getText();
    }
   
    private boolean caseNotSelected(){
    return!cbCase.isSelected();
    }
    private boolean isScarchDown(){
    return down.isSelected();
    }
   private boolean wholeWordIsSelected(){
   return cbWhole.isSelected();
   }
   private boolean checkForWholeWord(int check,String Text,int add,int caret){
   int offsetLeft=(caret + add)-1;
   int offsetRight=(caret + add)+check;
   if(offsetLeft<0||offsetRight>Text.length()){
   return true;
   }
      return((!Character.isLetterOrDigit(Text.charAt(offsetLeft)))&&(!Character.isLetterOrDigit(Text.charAt(offsetRight))));
              }
   private void replaceAll(){
String word= searchText.getText();
String Text=Java_Notepad.getArea().getText();
String insert=replaceText.getText();
StringBuffer sb= new StringBuffer(Text);
int diff=insert.length()-word.length();
int offset=0;
int tally=0;
       for (int i = 0; i < (Text.length()-word.length()); i++) {
          String temp =Text.substring(i,i+word.length());
          if((temp.equals(word) &&checkForWholeWord(word.length(),Text,0,i) )){
          tally++;
          sb.replace(i+ offset,i+offset+word.length(),insert);
          offset+=diff;
          }else{
          
          }
          Java_Notepad.getArea().setText(sb.toString());
           endResult(true, tally);
            Java_Notepad.getArea().setCaretPosition(0);
       }
       

}
}
