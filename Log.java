import javax.swing.*;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.awt.*;
import java.awt.event.*;

/**
 * Et objekt som skal kunne returnere feilmeldinger på
 * skjerm eller evt skrive dem til egen logg-fil alt
 * etter hva verdiene i boolean[] level er satt til
 * hvor 0 = skjerm, 1 = fil, 2 = messagebox
 * @author 	Egil Sørensen (egil@stud.ntnu.no)
 */

public abstract class Log {
	//Setter default verdi slik at det skrives til fil dersom ingenting annet oppgis
	//ved kjøring av setValue()

	private static boolean[] logLevel = {false, false, false};
    private static boolean[] errorLevel = {false, false, false};
    private static final int screen = 0;
    private static final int file = 1;
    private static final int msgbox = 2;



    /**
     * Setter log-levelen (hva loggen skal gjøre egentlig)
     * @param level
     */

    // ++========++======++==========++=========++
    // ++ Skjerm ++ Fil  ++ MsgBox   ++ Level   ++
    // ++========++======++==========++=========++
    // ++   0    ++  0   ++   0      ++    0    ++
    // ++========++======++==========++=========++
    // ++   0    ++  0   ++   1      ++    1    ++
    // ++========++======++==========++=========++
    // ++   0    ++  1   ++   0      ++    2    ++
    // ++========++======++==========++=========++
    // ++   0    ++  1   ++   1      ++    3    ++
    // ++========++======++==========++=========++
    // ++   1    ++  0   ++   0      ++    4    ++
    // ++========++======++==========++=========++
    // ++   1    ++  0   ++   1      ++    5    ++
    // ++========++======++==========++=========++
    // ++   1    ++  1   ++   0      ++    6    ++
    // ++========++======++==========++=========++
    // ++   1    ++  1   ++   1      ++    7    ++
    // ++========++======++==========++=========++


    public static void setLogLevel(int level)
    {
        switch (level)
        {
            case 0:
                setScreen(false, logLevel);
                setFile(false, logLevel);
                setMsgBox(false, logLevel);
            case 1:
                setScreen(false, logLevel);
                setFile(false, logLevel);
                setMsgBox(true, logLevel);
                break;
            case 2:
                setScreen(false, logLevel);
                setFile(true, logLevel);
                setMsgBox(false, logLevel);
                break;
            case 3:
                setScreen(false, logLevel);
                setFile(true, logLevel);
                setMsgBox(true, logLevel);
                break;
            case 4:
                setScreen(true, logLevel);
                setFile(false, logLevel);
                setMsgBox(false, logLevel);
                break;
            case 5:
                setScreen(true, logLevel);
                setFile(false, logLevel);
                setMsgBox(true, logLevel);
                break;
            case 6:
                setScreen(true, logLevel);
                setFile(true, logLevel);
                setMsgBox(false, logLevel);
                break;
            case 7:
                setScreen(true, logLevel);
                setFile(true, logLevel);
                setMsgBox(true, logLevel);
                break;
            default:
                System.out.println("Wrong loglevel");
         }

    }

    /**
     * Setter error-levelen (hva error skal gjøre egentlig)
     *
     *
     * @param level
     */

     // ++========++======++==========++=========++
     // ++ Skjerm ++ Fil  ++ MsgBox   ++ Level   ++
     // ++========++======++==========++=========++
     // ++   0    ++  0   ++   0      ++    0    ++
     // ++========++======++==========++=========++
     // ++   0    ++  0   ++   1      ++    1    ++
     // ++========++======++==========++=========++
     // ++   0    ++  1   ++   0      ++    2    ++
     // ++========++======++==========++=========++
     // ++   0    ++  1   ++   1      ++    3    ++
     // ++========++======++==========++=========++
     // ++   1    ++  0   ++   0      ++    4    ++
     // ++========++======++==========++=========++
     // ++   1    ++  0   ++   1      ++    5    ++
     // ++========++======++==========++=========++
     // ++   1    ++  1   ++   0      ++    6    ++
     // ++========++======++==========++=========++
     // ++   1    ++  1   ++   1      ++    7    ++
     // ++========++======++==========++=========++


      public static void setErrorLevel(int level)
      {
          switch (level)
          {
              case 0:
                  setScreen(false, errorLevel);
                  setFile(false, errorLevel);
                  setMsgBox(false, errorLevel);
              case 1:
                  setScreen(false, errorLevel);
                  setFile(false, errorLevel);
                  setMsgBox(true, errorLevel);
                  break;
              case 2:
                  setScreen(false, errorLevel);
                  setFile(true, errorLevel);
                  setMsgBox(false, errorLevel);
                  break;
              case 3:
                  setScreen(false, errorLevel);
                  setFile(true, errorLevel);
                  setMsgBox(true, errorLevel);
                  break;
              case 4:
                  setScreen(true, errorLevel);
                  setFile(false, errorLevel);
                  setMsgBox(false, errorLevel);
                  break;
              case 5:
                  setScreen(true, errorLevel);
                  setFile(false, errorLevel);
                  setMsgBox(true, errorLevel);
                  break;
              case 6:
                  setScreen(true, errorLevel);
                  setFile(true, errorLevel);
                  setMsgBox(false, errorLevel);
                  break;
              case 7:
                  setScreen(true, errorLevel);
                  setFile(true, errorLevel);
                  setMsgBox(true, errorLevel);
                  break;
              default:
                  System.out.println("Wrong errorlevel");
           }

      }








    /**
     * Skrur logging til skjerm av/på
     * @param value
     * @param level
     */
	private static void setScreen(boolean value, boolean[] level)
    {
        level[screen] = value;
    }


    /**
     * Skrur logging til fil av/på
     * @param value
     * @param level
     */
    private static void setFile(boolean value, boolean[] level)
    {
        level[file] = value;
    }

    /**
     *  Skrur logging til MsgBox av/på
     * @param value
     * @param level
     */
    private static void setMsgBox(boolean value, boolean[] level)
    {
        level[msgbox] = value;
    }


    /** Skriver ut debuggings-logg i hht logLevel
     *
     * @param element
     */

     public static void log(String element)
     {
        writeLog(element, "debug.apo", logLevel);
     }

    /** Skriver ut error-logg i hht errorLevel
     * @param element
     */

    public static void error(String element)
    {
        writeLog(element, "errorlog.apo", errorLevel);
    }



    /** Benyttes av error(String element) og log(String element)
     *  til å skrive loggen. Har med så jeg slipper skrive alt to ganger
     * @param element
     * @param filename
     */

     private static void writeLog(String element, String filename, boolean[] level)
    {
        if (level[screen])
        {
            System.out.println(element);
        }

        if (level[file])
        {
            try {
			FileWriter file = new FileWriter(filename,true);
			PrintWriter writer = new PrintWriter(file);
			//Lager standard dato-format og kalender objekt for å kunne
			//legg til timestamp til loggen
			GregorianCalendar cal = new GregorianCalendar();
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss");
			writer.println(sdf.format(cal.getTime()) + "::        " + element);
			writer.close();
			}
			catch (IOException io){
			    System.out.println("Exception making FileWriter");
			}
        }

        if (level[msgbox])
        {
            JOptionPane.showMessageDialog(null, element, "Critical Error", JOptionPane.ERROR_MESSAGE);
        }

    }
}


