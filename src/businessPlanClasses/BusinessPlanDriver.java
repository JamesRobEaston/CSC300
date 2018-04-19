package businessPlanClasses;
public class BusinessPlanDriver
{
   public BusinessPlanDriver()
   {
   }
   public static void main(String[] args)
   {
   
      //BUSINESS PLAN 1//d
      BusinessPlan org = new BusinessPlan("1997", "BP");
      org.addCategory("Vision", 1, 1, 1);
      org.addCategory("Mission", 2, 2, 4);
      org.addCategory("Objective", 3, 4, 8);
      org.setCategoryList();     
      Statement s1 = org.addStatement("Here is the text of our vision statement", "Vision", org.tree.getRoot());      
      Statement s2 = org.addStatement("First mission statement", "Mission", s1);      
      Statement s3 = org.addStatement("Second mission statement", "Mission", s1);
      Statement s4 = org.addStatement("Third mission statement", "Mission", s1);
      Statement s5 = org.addStatement("Fourth mission statement", "Mission", s1);
      
      
      assert s1.data.equals("Here is the text of our vision statement");
      assert s1.type.name.equals("Vision");
      //checking that s2's parent is s1
      assert s2.parent.equals(s1) : "s2's parent should assert to be s1."; 
      assert s1.children.contains(s2) : "s2 should be one of s1's children.";
      assert s1.children.contains(s3) : "s3 should be one of s1's children.";
      
      Statement s6 = org.addStatement("First objective statement", "Objective", s2);
      Statement s7 = org.addStatement("Second objective statement", "Objective", s2);
      Statement s8 = org.addStatement("Third objective statement", "Objective", s3);
      Statement s9 = org.addStatement("Fourth objective statement", "Objective", s3);
      Statement s10 = org.addStatement("Fifth objective statement", "Objective", s4);
      Statement s11 = org.addStatement("Sixth objective statement", "Objective", s4);
      Statement s12 = org.addStatement("Seventh objective statement", "Objective", s5);
      Statement s13 = org.addStatement("Eighth objective statement", "Objective", s5);
      
      assert s6.data.equals("First objective statement");
      assert s6.type.name.equals("Objective");
      assert s6.parent.equals(s2);
      assert s2.children.contains(s6);
      
      //Attempting to add a statement to a full category. Should print 3 error messages. 
      Statement s14 = org.addStatement("Ninth objective statement", "Objective", s5);
      Statement s15 = org.addStatement("Fifth mission statement", "Mission", s1);
      Statement s16 = org.addStatement("Second vision statement", "Vision", org.tree.getRoot()); 
      
      //Attempting to remove a statement to a category at/below its min # statements. 2 error messages.
      org.removeStatement(s1); //first error after this line
      org.removeStatement(s2);org.removeStatement(s3);org.removeStatement(s4); //second error after this line 
      
      //Removing statements
      org.removeStatement(s13);
      org.removeStatement(s12);
      assert s13.data == null;
      assert s13.parent == null;
      assert s13.children == null;
      assert s12.data == null;
      assert s12.parent == null;
      assert s12.children == null;
      
      //Testing if statements down the tree from removed statements are also removed 
      
      org.removeStatement(s2);
      assert s6.parent == null;
      assert s6.children == null;
      assert s6.data == null;
      
      //Editing statement data
      org.editStatement(s1, 0, "NEW MISSION STATEMENT!!!");
      assert s1.data.equals("NEW MISSION STATEMENT!!!");
      //If successful, will print a successfully stored message
      org.addToHistory();
      
      //BUSINESS PLAN 2//
      BusinessPlan org2 = new BusinessPlan("2008", "BP2");
      org2.addCategory("Heart", 1, 1, 1); //using random words to show the versatility of our software
      org2.addCategory("Brain", 2, 2, 4);
      org2.addCategory("Fire", 3, 4, 8);
      org2.setCategoryList();     
      Statement s01 = org2.addStatement("Here is the text of our heart statement", "Heart", org2.tree.getRoot());      
      Statement s02 = org2.addStatement("First brain statement", "Brain", s01);      
      Statement s03 = org2.addStatement("Second brain statement", "Brain", s01);
      Statement s04 = org2.addStatement("Third brain statement", "Brain", s01);
      Statement s05 = org2.addStatement("Fourth brain statement", "Brain", s01);
      Statement s06 = org2.addStatement("First fire statement", "Fire", s02);
      Statement s07 = org2.addStatement("Second fire statement", "Fire", s02);
      Statement s08 = org2.addStatement("Third fire statement", "Fire", s03);
      Statement s09 = org2.addStatement("Fourth fire statement", "Fire", s03);
      Statement s010 = org2.addStatement("Fifth fire statement", "Fire", s04);
      Statement s011 = org2.addStatement("Sixth fire statement", "Fire", s04);
      Statement s012 = org2.addStatement("Seventh fire statement", "Fire", s05);
      Statement s013 = org2.addStatement("Eighth fire statement", "Fire", s05);
      
      //Attempting to add a statement to a full category. Should print 3 error messages. 
      Statement s014 = org2.addStatement("Ninth fire statement", "Fire", s05);
      Statement s015 = org2.addStatement("Fifth brain statement", "Brain", s01);
      Statement s016 = org2.addStatement("Second Heart statement", "Heart", org2.tree.getRoot()); 
      
      //Attempting to remove a statement to a category at/below its min # statements. 2 error messages.
      org2.removeStatement(s01); //first error after this line
      org2.removeStatement(s02);org.removeStatement(s03);org.removeStatement(s04); //second error after this line 
      
      //Editing statement data
      org2.editStatement(s01, 0, "NEW BRAIN STATEMENT!!!");
      if(s01.getData().equals("NEW BRAIN STATEMENT!!!"))
      {
         System.out.println("Data successfully edited.");
      }
      else
      {  
         System.out.println("Data edit failure.");
      }
      //If successful, will print a successfully stored message
      org2.addToHistory();
      
      //BUSINESS PLAN 3//
      BusinessPlan org3 = new BusinessPlan("2013", "BP3");
      org3.addCategory("Lion", 1, 1, 1); //using random words to show the versatility of our software
      org3.addCategory("Tiger", 2, 2, 4);
      org3.addCategory("Bear", 3, 4, 8);
      org3.setCategoryList();     
      Statement s111 = org3.addStatement("Here is the text of our Lion statement", "Lion", org3.tree.getRoot());      
      Statement s112 = org3.addStatement("First Tiger statement", "Tiger", s111);      
      Statement s113 = org3.addStatement("Second Tiger statement", "Tiger", s111);
      Statement s114 = org3.addStatement("Third Tiger statement", "Tiger", s111);
      Statement s115 = org3.addStatement("Fourth Tiger statement", "Tiger", s111);
      Statement s116 = org3.addStatement("First Bear statement", "Bear", s112);
      Statement s117 = org3.addStatement("Second Bear statement", "Bear", s112);
      Statement s118 = org3.addStatement("Third Bear statement", "Bear", s113);
      Statement s119 = org3.addStatement("Fourth Bear statement", "Bear", s113);
      Statement s1110 = org3.addStatement("Fifth Bear statement", "Bear", s114);
      Statement s1111 = org3.addStatement("Sixth Bear statement", "Bear", s114);
      Statement s1112 = org3.addStatement("Seventh Bear statement", "Bear", s115);
      Statement s1113 = org3.addStatement("Eighth Bear statement", "Bear", s115);
      
      //Attempting to add a statement to a full category. Should print 3 error messages. 
      Statement s1114 = org3.addStatement("Ninth Bear statement", "Bear", s115);
      Statement s1115 = org3.addStatement("Fifth Tiger statement", "Tiger", s111);
      Statement s1116 = org3.addStatement("Second Lion statement", "Lion", org3.tree.getRoot()); 
      
      //Attempting to remove a statement to a category at/below its min # statements. 2 error messages.
      org3.removeStatement(s111); //first error after this line
      org3.removeStatement(s112);org.removeStatement(s113);org.removeStatement(s114); //second error after this line 
      
      //Editing statement data
      org3.editStatement(s111, 0, "NEW TIGER STATEMENT!!!");
      if(s111.getData().equals("NEW TIGER STATEMENT!!!"))
      {
         System.out.println("Data successfully edited.");
      }
      else
      {  
         System.out.println("Data edit failure.");
      }
      //If successful, will print a successfully stored message
      org3.addToHistory();
    }
}