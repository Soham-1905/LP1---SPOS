import java.util.*;
import java.io.*;

public class pass1 
{

    static int address = 0;
    static int sadd[] = new int[10];
    static int ladd[] = new int[10];

    public static void main(String args[]) 
    {
        BufferedReader br;
        String input = null;

        String IS[] = {"ADD", "SUB", "MUL", "MOV"};
        String UserReg[] = {"AREG", "BREG", "CREG", "DREG"}; 
        String AD[] = {"START", "END"};
        String DL[] = {"DC", "DS"};

        int scount = 0, lcount = 0;
        int flag = 0; 
        String tt = null;

        String sv[] = new String[10];
        String lv[] = new String[10];

        try 
        {
            br = new BufferedReader(new FileReader("initial.txt"));
            File f = new File("IM.txt");
            File f1 = new File("ST.txt");
            File f2 = new File("LT.txt");
            PrintWriter p = new PrintWriter(f);
            PrintWriter p1 = new PrintWriter(f1);
            PrintWriter p2 = new PrintWriter(f2);

            while ((input = br.readLine()) != null) 
            {
                StringTokenizer st = new StringTokenizer(input, " ");
                
                boolean isFirstToken = true;
                int dsSize = 0;

                while (st.hasMoreTokens()) 
                {
                    tt = st.nextToken();
                    
                    boolean isRegister = false;
                    boolean isInstruction = false;

                    // --- Label Detection ---
                    if(isFirstToken == true)
                    {
                        isFirstToken = false; 
                        boolean isMnem = false;
                        // Fixed: Using .equals() for all string comparisons
                        for(int i=0; i<IS.length; i++) { if(IS[i].equals(tt)) isMnem = true; }
                        for(int i=0; i<AD.length; i++) { if(AD[i].equals(tt)) isMnem = true; }
                        for(int i=0; i<DL.length; i++) { if(DL[i].equals(tt)) isMnem = true; }
                        
                        if(isMnem == false) // It's a label
                        {
                            int s_index = -1;
                            for(int i=0; i<scount; i++)
                            {
                                if(sv[i].equals(tt))
                                {
                                    s_index = i;
                                    break;
                                }
                            }
                            if(s_index == -1)                             {
                                sv[scount] = tt;
                                s_index = scount;
                                scount++;
                            }
                            sadd[s_index] = address; 
                            continue; 
                        }
                    }
                    // --- END Label Detection ---


                    for (int i = 0; i < AD.length; i++) 
                    {
                        if (tt.equals(AD[i]))
                        {
                            p.print("AD " + (i + 1) + " ");
                            if (tt.equals("START") && st.hasMoreTokens()) 
                            {
                                String startAddr = st.nextToken();
                                p.println(startAddr);
                                address = Integer.parseInt(startAddr) - 1;
                            }
                        }
                    }
                    
                    for (int i = 0; i < IS.length; i++)
                        if (tt.equals(IS[i])) 
                        {
                            p.print("IS " + (i + 1) + " ");
                            isInstruction = true; 
                        }
                    for (int i = 0; i < UserReg.length; i++)
                        if (tt.equals(UserReg[i])) 
                        {
                            p.print((i + 1) + " ");
                            flag = 1;
                            isRegister = true;
                        }
                    
                    
                    for (int i = 0; i < DL.length; i++) 
                    {
                        if (tt.equals(DL[i])) 
                        {
                            p.print("DL " + (i + 1) + " ");
                            flag = 0;
                            
                            if (tt.equals("DS")) 
                            {
                                String size = st.nextToken(); 
                                p.print(size + " ");
                                dsSize = Integer.parseInt(size);
                            }
                            else if (tt.equals("DC")) 
                            {
                                String constant = st.nextToken();
                                p.print(constant + " ");
                            }
                        }
                    }

                    
                    if (tt.startsWith("=")) 
                    {
                        p.print("L" + lcount + " ");
                        lv[lcount] = tt;
                        ladd[lcount] = 0; 
                        lcount++;
                        flag = 0; 
                    }
                    
                    else if (flag == 1 && isRegister == false && isInstruction == false) 
                    { 
                        
                        int s_index = -1;
                        for (int i = 0; i < scount; i++) 
                        {
                            if (sv[i].equals(tt))
                            {
                                s_index = i;
                                break;
                            }
                        }

                        if (s_index != -1) {
                            p.print("S" + s_index + " ");
                        } else { 
                            p.print("S" + scount + " ");
                            sv[scount] = tt;
                            scount++;
                        }
                        flag = 0; 
                    }

                } 
                p.println("");
                
                address++;
                if (dsSize > 0) {
                    address += (dsSize - 1);
                    dsSize = 0;
                }
            } 

            address--; 

            p1.println("Index\tSymbol\tAddress");
            p1.println("-------------------------");
            for (int i = 0; i < scount; i++) {
                p1.println(i + "\t" + sv[i] + "\t" + sadd[i]);
            }

            p2.println("Index\tLiteral\tAddress");
            p2.println("-------------------------");
            for (int i = 0; i < lcount; i++) {
                ladd[i] = address; 
                p2.println(i + "\t" + lv[i] + "\t" + address);
                address++;
            }

            br.close();
            p.close();
            p1.close();
            p2.close();
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
    }
}