package mac;

import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;
import java.util.Date;
import java.util.Calendar;
import java.util.TimeZone;
import javax.microedition.rms.RecordComparator;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreNotOpenException;
import javax.microedition.rms.InvalidRecordIDException;
import javax.microedition.rms.RecordStoreException;
import javax.microedition.rms.RecordEnumeration;
import java.io.InputStream;
import java.io.IOException;
import javax.microedition.rms.RecordEnumeration;
import java.io.PrintStream;
import java.io.IOException;
import java.util.*;
import javax.microedition.rms.RecordFilter;
import java.io.*;
import javax.microedition.io.*;

public class DTMFRequestSony extends MIDlet implements CommandListener,
        Runnable {

    private Command DialCommand, ExitCommand, AboutCommand, statusCommand,
    goToBackInfoCommand,
    cancelCommand, lunchSendCommand, lunchBackCommand,
    groupcommand, sendCommand1, cancelCommand1,
    busySendCommand, busyBackCommand, meetingSendCommand, meetingBackCommand,
    travelSendCommand, travelBackCommand,
    sickSendCommand, sickBackCommand, vacationSendCommand, vacationBackCommand,
    courseSendCommand, courseBackCommand, logInSendCommand,
    logInBackCommand, logOutSendCommand, logOutBackCommand, minimazeCommand,
    propertiesCommand, settingsCommand, helpCommand, backCommand,
    editSettingBackCommand,
    editSettingCancelCommand, editSettingSaveCommand, ExitCommandMainList,
    listBackCommand, cmdBack, cmdDial, emptyCallListCommand,

    cmdShortAddCommand, cmdShortDial, cmdShortBack, emptyShortCallListCommand,
    cmdShortAdd, cmdShortCancel, backShortFormCommand, cancelShortFormCommand,
    addShortFormCommand,

    CallForwardDialCommand, CallForwardBackCommand, callForwardListBackCommand,
    transferCallForwardCancelCommand, transferCallForwardDialCommand,
    abortTransferCallForwardDialCommand, abortTransferCallForwardBackCommand,
    DELETECommand, shortNumbersAddCommand, shortNumbersCancelCommand,
    shortNumbersBackCommand, shortNumbersEditCommand,
    confirmExitYESCommand, confirmExitNOCommand, voiceBackCommand,
    confirmDeleteCallListYESCommand, confirmDeleteCallListNOCommand,
    confirmDeleteShortListYESCommand, confirmDeleteShortListNoCommand,

    dialBackCommand, freeTimeSendCommand, freeTimeBackCommand, aboutMobismaCommand, goBackCommand;


    private Ticker lunchTicker, busyTicker, meetingTicker, travelTicker,
    sickTicker, vacationTicker, courseTicker, menuTicker, mainTicker,
    groupTicker, dialTicker, loginTicker, logoutTicker, callForwardTicker,
    callForewardPresentTicker, abortTransferCallForwardTicker, voiceMailTicker,
    freeTimeTicker;


    private Command thCmd;
    private String stringTotal;
    private int type = 0;
    private String SOS; // Nokia >> ('p' hela serie 40) ('/' , '/p' serie 60)
    private String setP = "/"; // Sonyericsson >> (';postd=' för mobiltelefoner)  ('/p' för pda-modeller)
    private String accessCode, internNumber;
    private String SerieNumber = "00460101501594500"; // Imei nummer. telefonens serienummer
    private String IMEI;

    private String identy, checkIdenty;
    private String sortString, Star;
    private String[] subStr;
    private String accessNumber, switchBoardNumber, star, setDayDate, setDate,
            voicemailNumber;
    private Alert alert, alertEditSettings, alertRestarting, alertExit,
    alertDeleteCallListAllInfo, alertDeleteShortListAllInfo,
    alertON, alertOFF, alertDebugNOK;

    private List referenceList, groupList, mainList, nameList, shortList,
    callForwardList, voiceMailList;

    public RecordStore recStore = null;
    static final String REC_STORE = "Data_Store_attendant_01";

    private int saveRecordID;
    private int antalDagar = 30;
    private Date today;
    private TimeZone tz = TimeZone.getTimeZone("GMT+1");

    String s1;
    String s2;

    private String setMounth, setYear,
    DBdate, DBmounth, DByear, DBdateBack, DBmounthBack, DByearBack, getTWO,
    dateString, setViewMounth, ViewDateString, setdayBack,
    setmounthBack, setyearBack;

    private Form editSettingForm, shortForm, addShortForm,
   transferForwardCallForm, editShortNumbersForm, dialForm,

   lunchForm, busyForm, meetingForm, travelForm, sickForm, vacationForm,
   courseForm,

   loginForm, logoutForm,

   callforwardpresentForm, aborttransfercallForm, freeTimeForm;


    private TextField voiceMailNumber, accessNumbers, editSwitchBoardNumber,
    e_firstNameShort, e_lastNameShort, e_phoneNumShort, ownNumberTextField,
    newNumberTextField,

    editShortNumberTextFieldName, editShortNumberTextFieldSurname,
    editShortNumberTextFieldExtension, dialTextField,

    lunchTextField, outTextField, meetingTextField, travelTextField,
    sickTextField, vacationTextField, courseTextField,

    loginTextField, logoutTextField,

    callforwardpresentTextField, aborttransfercallTextField, freeTimeTextField;

    private int dayBack;
    private int mounthBack;
    private int yearBack;
    private int dayAfter;
    private int monthAfter;
    private int yearAfter;
    private int day;
    private int month;
    private int checkYear;

    private Vector phoneNums;
    private Vector shortNums;

    private int sortOrder = 1;

    private RecordStore addrBook;
    private RecordStore shortNumbers;
    private static String url = "socket://127.0.0.1:8100";
    private String request;
    private String response;
    public String ResponceMessage;
    private static final int FN_LEN = 10;
    private static final int LN_LEN = 20;
    private static final int PN_LEN = 15;
    final private static int ERROR = 0;
    final private static int INFO = 1;
    final private static int SIZE = 2;
    final private static int INPUT = 3;
    final private static int NAMEERROR = 4;

    private Display displayen, display;


    public DTMFRequestSony() throws InvalidRecordIDException,
            RecordStoreNotOpenException, RecordStoreException, IOException {

        alert = new Alert("", "", null, AlertType.INFO);
        alert.setTimeout(2000);

        try {
            addrBook = RecordStore.openRecordStore("TheAddressBook", true); // skapar tabeller i databasen
        } catch (RecordStoreException e) {
            addrBook = null;
        }

        try {
            shortNumbers = RecordStore.openRecordStore("ShortNumberList", true);
        } catch (RecordStoreException e) {
            shortNumbers = null;
        }

        this.tz = tz;

        today = new Date();
        today.getTime();
        today.toString();

        System.out.println(today);

        this.antalDagar = antalDagar; // anger hur många dagar programmet ska vara öppet innan det stängs....

        try {
            this.accessNumber = getAccessNumber();
        } catch (RecordStoreNotOpenException ex1) {
        } catch (InvalidRecordIDException ex1) {
        } catch (RecordStoreException ex1) {
        }
        try {
            this.switchBoardNumber = getSwitchBoardNumber();
        } catch (RecordStoreNotOpenException ex2) {
        } catch (InvalidRecordIDException ex2) {
        } catch (RecordStoreException ex2) {
        }
        this.setDayDate = setDayDate;
        this.setDate = setDate;
        this.star = star;
        this.accessCode = accessCode;
        this.internNumber = internNumber;
        this.accessNumber = accessNumber;

        this.setP = setP;
        this.SOS = "112";
        this.identy = ""; // System.getProperty("com.sonyericsson.imei");
        this.checkIdenty = checkIdenty;
        this.SerieNumber = SerieNumber.trim();

        try {
            this.accessNumber = getAccessNumber();
        } catch (RecordStoreNotOpenException ex1) {
        } catch (InvalidRecordIDException ex1) {
        } catch (RecordStoreException ex1) {
        }
        try {
            this.voicemailNumber = getVoiceMailNumber();
        } catch (RecordStoreNotOpenException ex2) {
        } catch (InvalidRecordIDException ex2) {
        } catch (RecordStoreException ex2) {
        }
        this.accessCode = accessCode;
        this.internNumber = internNumber;
        this.accessNumber = accessNumber;

        this.IMEI = identy;
        this.star = "1";

        this.day = day;
        this.month = month;
        setDBDate(); // OBS.. Det här metodanropet ska ligga här efter month och day.
        setDBDateBack();

        //--------------- Alert-Screen -----------------------------------------

        //--------------- Alert on ---------------------------------------------

        try {

                Image alertRestartImage = Image.createImage("/icon/info.png");
                alertON = new Alert("Mex On!", "", alertRestartImage, AlertType.INFO);
                alertON.setTimeout(1000);


            } catch (IOException ex11) {
            }

        //--------------- Alert off --------------------------------------------

        try {

                Image alertRestartImage = Image.createImage("/icon/restart.png");
                alertOFF = new Alert("Mex Off!", "", alertRestartImage, AlertType.INFO);
                alertOFF.setTimeout(1000);


            } catch (IOException ex11) {
            }



        //--------------- Alert Deboug NOK -------------------------------------

            try {

                Image alertRestartImage = Image.createImage("/icon/restart.png");
                alertDebugNOK = new Alert("Server Error!", "", alertRestartImage, AlertType.INFO);
                alertDebugNOK.setTimeout(Alert.FOREVER);


            } catch (IOException ex11) {
            }


        // DeleteAllShortList

        try {
            Image alertExitImage = Image.createImage("/icon/exit2_64.png");
            alertDeleteShortListAllInfo = new Alert("Radera Alla!",
                                  "Vill du radera alla anknytningar" + '?' + "\n Ja eller Nej?",
                                  alertExitImage, AlertType.CONFIRMATION);

            alertDeleteShortListAllInfo.setTimeout(Alert.FOREVER);

            confirmDeleteShortListYESCommand = new Command("Ja",
                                                Command.OK, 1);
            confirmDeleteShortListNoCommand = new Command("Nej",
                                               Command.CANCEL, 2);

            alertDeleteShortListAllInfo.addCommand(confirmDeleteShortListYESCommand);
            alertDeleteShortListAllInfo.addCommand(confirmDeleteShortListNoCommand);
            alertDeleteShortListAllInfo.setCommandListener(this);
        } catch (IOException ex5) {
        }


        //DeleteAllCallList alert.....

        try {
            Image alertExitImage = Image.createImage("/icon/exit2_64.png");
            alertDeleteCallListAllInfo = new Alert("Radera Alla!",
                                  "Vill du radera samtalslistan" + '?' + "\n Ja eller Nej?",
                                  alertExitImage, AlertType.CONFIRMATION);

            alertDeleteCallListAllInfo.setTimeout(Alert.FOREVER);

            confirmDeleteCallListYESCommand = new Command("Ja",
                                                Command.OK, 1);
            confirmDeleteCallListNOCommand = new Command("Nej",
                                               Command.CANCEL, 2);

            alertDeleteCallListAllInfo.addCommand(confirmDeleteCallListYESCommand);
            alertDeleteCallListAllInfo.addCommand(confirmDeleteCallListNOCommand);
            alertDeleteCallListAllInfo.setCommandListener(this);
        } catch (IOException ex5) {
        }


        //Exit alert....
        try {
            Image alertExitImage = Image.createImage("/icon/exit2_64.png");
            alertExit = new Alert("Avsluta",
                                  "Vill du avsluta" + '?' + "\n Ja eller Nej?",
                                  alertExitImage, AlertType.CONFIRMATION);

            alertExit.setTimeout(Alert.FOREVER);

            confirmExitYESCommand = new Command("Ja",
                                                Command.EXIT, 1);
            confirmExitNOCommand = new Command("Nej",
                                               Command.OK, 2);

            alertExit.addCommand(confirmExitYESCommand);
            alertExit.addCommand(confirmExitNOCommand);
            alertExit.setCommandListener(this);
        } catch (IOException ex5) {
        }

        //--------------- Alert edit Settings ----------------------------------

        try {
            Image alertEditSettingImage = Image.createImage("/icon/save.png");
            alertEditSettings = new Alert("Sparar Ändringar",
                                          "\n\n\n...Ändringar sparas... ",
                                          alertEditSettingImage,
                                          AlertType.CONFIRMATION);
            setDataStore();
            upDateDataStore();
            alertEditSettings.setTimeout(2000);
        } catch (InvalidRecordIDException ex7) {
        } catch (RecordStoreNotOpenException ex7) {
        } catch (RecordStoreException ex7) {
        } catch (IOException ex7) {
        }

        //--------------- Alert Restart program --------------------------------

        try {
            Image alertRestartImage = Image.createImage("/icon/restart.png");
            alertRestarting = new Alert("Sparar Ändringar",
                                        "\n\n\n...Starta om...Programmet... ",
                                        alertRestartImage,
                                        AlertType.CONFIRMATION);
            setDataStore();
            upDateDataStore();
            alertRestarting.setTimeout(Alert.FOREVER);
        } catch (InvalidRecordIDException ex9) {
        } catch (RecordStoreNotOpenException ex9) {
        } catch (RecordStoreException ex9) {
        } catch (IOException ex9) {
        }

        //------------ Alert info ----------------------------------------------

        try {
            Image alertInfoImage = Image.createImage("/icon/info.png");

            alert = new Alert("", "", alertInfoImage, AlertType.INFO);
            alert.setTimeout(2000);
        } catch (IOException ex) {
        }

        //--------------- Logout-textbox ---------------------------------------

        logoutForm = new Form("");

        logoutTextField = new TextField("Skriv in Siffror", "", 6, TextField.NUMERIC);

        logoutTicker = new Ticker("Logga ur grupp");
        logoutForm.setTicker(logoutTicker);

        logOutSendCommand = new Command("Skicka", Command.OK, 1);
        logOutBackCommand = new Command("Bakåt", Command.BACK, 4); // Kommando hör till graphic-vyn för infot.

        logoutForm.addCommand(logOutSendCommand);
        logoutForm.addCommand(logOutBackCommand);
        logoutForm.setCommandListener(this);

        //--------------- Login-textbox ---------------------------------------

        loginForm = new Form("");

        loginTextField = new TextField("Skriv in Siffror", "", 6, TextField.NUMERIC);

        loginTicker = new Ticker("Logga in grupp");
        loginForm.setTicker(loginTicker);

        logInSendCommand = new Command("Skicka", Command.OK, 1);
        logInBackCommand = new Command("Bakåt", Command.BACK, 4); // Kommando hör till graphic-vyn för infot.

        loginForm.addCommand(logInSendCommand);
        loginForm.addCommand(logInBackCommand);
        loginForm.setCommandListener(this);

        //------------ Medflyttning --------------------------------------------

        transferForwardCallForm = new Form("Medflyttning");

        ownNumberTextField = new TextField("Eget nummer: ", "", 20,
                                           TextField.PHONENUMBER);
        newNumberTextField = new TextField("Nytt nummer: ", "", 20,
                                           TextField.PHONENUMBER);

        transferCallForwardCancelCommand = new Command("Bakåt", Command.BACK, 1);
        transferCallForwardDialCommand = new Command("Vidarekoppla", Command.OK,
                2);

        transferForwardCallForm.addCommand(transferCallForwardCancelCommand);
        transferForwardCallForm.addCommand(transferCallForwardDialCommand);
        transferForwardCallForm.setCommandListener(this);

        // ------------ Add-Edit-short-extensions -----------------------------

        editShortNumbersForm = new Form("Redigera");

        editShortNumberTextFieldName = new TextField("Namn:", "", 12,
                TextField.ANY);
        editShortNumberTextFieldSurname = new TextField("Efternamn:", "", 20,
                TextField.ANY);
        editShortNumberTextFieldExtension = new TextField("Anknytning:", "", 5,
                TextField.PHONENUMBER);

        shortNumbersAddCommand = new Command("Spara", Command.OK, 1);
        shortNumbersBackCommand = new Command("Bakåt", Command.BACK, 2);
        shortNumbersCancelCommand = new Command("Avbryt", Command.BACK, 3);

        editShortNumbersForm.addCommand(shortNumbersAddCommand);
        editShortNumbersForm.addCommand(shortNumbersCancelCommand);
        editShortNumbersForm.addCommand(shortNumbersBackCommand);
        editShortNumbersForm.setCommandListener(this);

        //------------ Add-Short-Form ------------------------------------------

        addShortForm = new Form("Ring anknytning");

        addShortFormCommand = new Command("Lägg till", Command.OK, 1);
        backShortFormCommand = new Command("Bakåt", Command.BACK, 2);
        cancelShortFormCommand = new Command("Avbryt", Command.BACK, 3);

        addShortForm.addCommand(backShortFormCommand);
        addShortForm.addCommand(cancelShortFormCommand);
        addShortForm.addCommand(addShortFormCommand);
        addShortForm.setCommandListener(this);

//---------------- EDITSETTINGFORM ---------------------------------------------

        editSettingForm = new Form("Egenskaper");

        accessNumbers = new TextField("Accessnummer: ", "", 32,
                                      TextField.NUMERIC);

        editSwitchBoardNumber = new TextField("Växelnummer: ", "", 32,
                                              TextField.PHONENUMBER);

        voiceMailNumber = new TextField("Röstbrevlåda: ", "", 32,
                                        TextField.PHONENUMBER);

        AboutCommand = new Command("Version 2.0", Command.HELP, 1);
        helpCommand = new Command("Hjälp", Command.HELP, 2);

        editSettingBackCommand = new Command("Bakåt", Command.BACK, 1);
        editSettingCancelCommand = new Command("Avbryt", Command.BACK, 1);
        editSettingSaveCommand = new Command("Spara", Command.OK, 2);

        editSettingForm.addCommand(editSettingBackCommand);
        editSettingForm.addCommand(editSettingCancelCommand);
        editSettingForm.addCommand(editSettingSaveCommand);
        editSettingForm.setCommandListener(this);

        //--------------- Lunch-textbox ---------------------------------------

        lunchForm = new Form("");

        lunchTextField = new TextField("Skriv in HHMM", "", 4, TextField.NUMERIC);

        lunchTicker = new Ticker("Lunch Åter");

        lunchSendCommand = new Command("Hänvisa", Command.OK, 1);
        lunchBackCommand = new Command("Bakåt", Command.BACK, 4); // Kommando hör till graphic-vyn för infot.
        lunchForm.setTicker(lunchTicker);
        lunchForm.addCommand(lunchSendCommand);
        lunchForm.addCommand(lunchBackCommand);
        lunchForm.setCommandListener(this);

        //--------------- Ledig ------------------------------------------------

        freeTimeForm = new Form("");

        freeTimeTextField = new TextField("Skriv in MMDD", "", 4, TextField.NUMERIC);

        freeTimeTicker = new Ticker("Ledig");

        freeTimeSendCommand = new Command("Hänvisa", Command.OK, 1);
        freeTimeBackCommand = new Command("Bakåt", Command.BACK, 4); // Kommando hör till graphic-vyn för infot.

        freeTimeForm.setTicker(freeTimeTicker);
        freeTimeForm.addCommand(freeTimeSendCommand);
        freeTimeForm.addCommand(freeTimeBackCommand);
        freeTimeForm.setCommandListener(this);


        //--------------- Tillfälligt ute -------------------------------------

        busyForm = new Form("");

        outTextField = new TextField("Skriv in HHMM", "", 4, TextField.NUMERIC);

        busyTicker = new Ticker("Upptagen till");

        busySendCommand = new Command("Hänvisa", Command.OK, 1);
        busyBackCommand = new Command("Bakåt", Command.BACK, 4); // Kommando hör till graphic-vyn för infot.

        busyForm.setTicker(busyTicker);
        busyForm.addCommand(busySendCommand);
        busyForm.addCommand(busyBackCommand);
        busyForm.setCommandListener(this);
        //--------------- Sammanträde -------------------------------------

        meetingForm = new Form("");

        meetingTextField = new TextField("Skriv in HHMM", "", 4, TextField.NUMERIC);

        meetingTicker = new Ticker("Möte Tillbaka");
        meetingSendCommand = new Command("Hänvisa", Command.OK, 1);
        meetingBackCommand = new Command("Bakåt", Command.BACK, 4); // Kommando hör till graphic-vyn för infot.

        meetingForm.setTicker(meetingTicker);
        meetingForm.addCommand(meetingSendCommand);
        meetingForm.addCommand(meetingBackCommand);
        meetingForm.setCommandListener(this);

        //--------------- Tjänsteresa -------------------------------------

        travelForm = new Form("");

        travelTextField = new TextField("Skriv in MMDD", "", 4, TextField.NUMERIC);
        travelTicker = new Ticker("Tjänsteresa");
        travelSendCommand = new Command("Hänvisa", Command.OK, 1);
        travelBackCommand = new Command("Bakåt", Command.BACK, 4); // Kommando hör till graphic-vyn för infot.

        travelForm.setTicker(travelTicker);
        travelForm.addCommand(travelSendCommand);
        travelForm.addCommand(travelBackCommand);
        travelForm.setCommandListener(this);

        //--------------- Sjuk -------------------------------------

        sickForm = new Form("");

        sickTextField = new TextField("Skriv in MMDD", "", 4, TextField.NUMERIC);
        sickTicker = new Ticker("Sjuk");
        sickSendCommand = new Command("Hänvisa", Command.OK, 1);
        sickBackCommand = new Command("Bakåt", Command.BACK, 4); // Kommando hör till graphic-vyn för infot.

        sickForm.setTicker(sickTicker);
        sickForm.addCommand(sickSendCommand);
        sickForm.addCommand(sickBackCommand);
        sickForm.setCommandListener(this);

        //--------------- Semester -------------------------------------

        vacationForm = new Form("");

        vacationTextField = new TextField("Skriv in MMDD", "", 4, TextField.NUMERIC);
        vacationTicker = new Ticker("Semester");
        vacationSendCommand = new Command("Hänvisa", Command.OK, 1);
        vacationBackCommand = new Command("Bakåt", Command.BACK, 4); // Kommando hör till graphic-vyn för infot.

        vacationForm.setTicker(vacationTicker);
        vacationForm.addCommand(vacationSendCommand);
        vacationForm.addCommand(vacationBackCommand);
        vacationForm.setCommandListener(this);

        //--------------- Gått för dagen -------------------------------------

        courseForm = new Form("");

        courseTextField = new TextField("Skriv in MMDD", "", 4,
                                       TextField.NUMERIC);
        courseTicker = new Ticker("Kurs Tillbaka");
        courseSendCommand = new Command("Hänvisa", Command.OK, 1);
        courseBackCommand = new Command("Bakåt", Command.BACK, 4); // Kommando hör till graphic-vyn för infot.

        courseForm.setTicker(courseTicker);
        courseForm.addCommand(courseSendCommand);
        courseForm.addCommand(courseBackCommand);
        courseForm.setCommandListener(this);

        //------------- VoiceMail List -----------------------------------------

        voiceMailList = new List("Röstbrevlådan", Choice.IMPLICIT); // skapar en lista
        voiceMailList.setTitle(null);
        voiceMailTicker = new Ticker("Röstbrevlådan");
        voiceMailList.setTicker(voiceMailTicker);

        voiceBackCommand = new Command("Bakåt", Command.BACK, 1);

        try {
            Image voiceImage = Image.createImage("/icon/rostbrevlada24.png");

            voiceMailList.append("Beställa", voiceImage);
            voiceMailList.append("Avbeställa", voiceImage);
            voiceMailList.append("Lyssna", voiceImage);

        } catch (IOException ex4) {
        }

        voiceMailList.addCommand(voiceBackCommand);
        voiceMailList.setCommandListener(this);

        //------------- MainList -----------------------------------------------

        mainList = new List("", Choice.IMPLICIT); // skapar en lista
        mainList.setTitle(null);
        mainTicker = new Ticker("mobismaME");
        mainList.setTicker(mainTicker);

        propertiesCommand = new Command("Redigera", Command.OK, 1);
        aboutMobismaCommand = new Command("Om mobisma", Command.OK, 2);
        ExitCommandMainList = new Command("Avsluta", Command.OK, 3);


        try {

            Image image1a = Image.createImage("/icon/ring24.png");
            Image image2a = Image.createImage("/icon/samtalslista24.png");
            Image image3a = Image.createImage("/icon/systemphone24.png");
            Image image4a = Image.createImage("/icon/hanvisa24.png");
            Image image5a = Image.createImage("/icon/konference24.png");
            Image image6a = Image.createImage("/icon/vidarekoppling24.png");
            Image image7a = Image.createImage("/icon/rostbrevlada24.png");
            Image image8a = Image.createImage("/icon/on24.png");
            Image image9a = Image.createImage("/icon/off24.png");
            Image image10a = Image.createImage("/icon/minimera24.png");

            /*mainList.append("Ring", image1a);
            mainList.append("Samtalslista", image2a);
            mainList.append("Ring anknytning", image3a);*/
            mainList.append("Hänvisa", image4a);
            mainList.append("Grupper", image5a);
            mainList.append("Vidarekoppla", image6a);
            mainList.append("Röstbrevlåda", image7a);
            mainList.append("Mex on", image8a);
            mainList.append("Mex off", image9a);
            mainList.append("Minimera", image10a);

            mainList.addCommand(ExitCommandMainList);
            mainList.addCommand(propertiesCommand);
            mainList.addCommand(aboutMobismaCommand);
            mainList.setCommandListener(this);


        } catch (IOException ex) {
            System.out.println("Unable to Find or Read .png file");
        }

        //-------------- Vidarekoppling ---------------------------------------

        callForwardList = new List("", Choice.IMPLICIT); // skapar en lista
        callForwardList.setTitle(null);
        callForwardTicker = new Ticker("Vidarekoppling");

        callForwardList.setTicker(callForwardTicker);
        try {
            Image image1b = Image.createImage("/icon/vidarekoppling24.png");
            Image image2b = Image.createImage("/icon/vidarekoppling24.png");
            Image image3b = Image.createImage("/icon/taborthanvisning24.png");
            Image image4b = Image.createImage("/icon/vidarekoppling24.png");
            Image image5b = Image.createImage("/icon/taborthanvisning24.png");

            callForwardList.append("Fast vidarekoppling", image1b);
            callForwardList.append("Intern hänvisning", image2b);
            callForwardList.append("Medflyttning", image4b);
            callForwardList.append("Avbryt medflyttning", image5b);
            callForwardList.append("Avbryt Fast/Intern", image3b);

            callForwardListBackCommand = new Command("Bakåt", Command.BACK, 1);

            callForwardList.addCommand(callForwardListBackCommand);
            callForwardList.setCommandListener(this);
        } catch (IOException ex) {
            System.out.println("Unable to Find or Read .png file");
        }

        //--------------- Internhänvisning -------------------------------------

        callforwardpresentForm = new Form("");

        callforwardpresentTextField = new TextField("Ange nummer", "", 20, TextField.PHONENUMBER);

        callForewardPresentTicker = new Ticker("Intern hänvisning");

        CallForwardDialCommand = new Command("Vidarekoppla", Command.OK, 1);
        CallForwardBackCommand = new Command("Bakåt", Command.BACK, 4);

        callforwardpresentForm.setTicker(callForewardPresentTicker);

        callforwardpresentForm.addCommand(CallForwardDialCommand);
        callforwardpresentForm.addCommand(CallForwardBackCommand);
        callforwardpresentForm.setCommandListener(this);

        //-------------- Avbryt medflyttning ----------------------------------

        aborttransfercallForm = new Form("");


        aborttransfercallTextField = new TextField("Eget nummer:", "", 20, TextField.PHONENUMBER);

        abortTransferCallForwardTicker = new Ticker("Avbryt medflyttning");

        abortTransferCallForwardDialCommand = new Command("Ring (via PBX", Command.OK, 1);
        abortTransferCallForwardBackCommand = new Command("Bakåt", Command.BACK,
                4);

        aborttransfercallForm.setTicker(
                abortTransferCallForwardTicker);

        aborttransfercallForm.addCommand(
                abortTransferCallForwardDialCommand);
        aborttransfercallForm.addCommand(
                abortTransferCallForwardBackCommand);
        aborttransfercallForm.setCommandListener(this);

        //-------------- LISTA ------------------------------------------------
        referenceList = new List("", Choice.IMPLICIT); // skapar en lista
        referenceList.setTitle(null);
        menuTicker = new Ticker("Hänvisning");

        referenceList.setTicker(menuTicker);
        try {
            Image image1 = Image.createImage("/icon/lunch24.png");
            Image image2 = Image.createImage("/icon/ute24.png");
            Image image3 = Image.createImage("/icon/konference24.png");
            Image image4 = Image.createImage("/icon/bortrest24.png");
            Image image5 = Image.createImage("/icon/sjuk24.png");
            Image image6 = Image.createImage("/icon/semester24.png");
            Image image7 = Image.createImage("/icon/upptagen24.png");
            Image image9 = Image.createImage("/icon/taborthanvisning24.png");
            Image image10 = Image.createImage("/icon/tillbakakl24.png");
            Image image11 = Image.createImage("/icon/kortnummer24.png");
            Image image12 = Image.createImage("/icon/tillbakaden24.png");

            referenceList.append("Ta bort hänvisning", image9); // attribut
            referenceList.append("Lunch Åter", image1); // attribut
            referenceList.append("Upptagen till", image10); // attribut
            referenceList.append("Frånvaro", image2); // attribut
            referenceList.append("Möte tillbaka", image3); // attribut
            referenceList.append("Tjänsteresa", image4); // attribut
            referenceList.append("Kurs tillbaka", image11); // attribut
            referenceList.append("Semester tillbaka", image6); // attribut
            referenceList.append("Ledig", image12); // attribut
            referenceList.append("Gått För Dagen", image7); // attribut
            referenceList.append("Sjuk", image5); // attribut

            listBackCommand = new Command("Bakåt", Command.BACK, 1);

            referenceList.addCommand(listBackCommand);
            referenceList.setCommandListener(this);
        } catch (IOException ex) {
            System.out.println("Unable to Find or Read .png file");
        }

        //--------------Grupp LISTA -------------------------------------------
        groupList = new List("Grupper", Choice.IMPLICIT); // skapar en lista

        groupList.setTitle(null);

        groupTicker = new Ticker("Grupper ACD");
        groupList.setTicker(groupTicker);

        //sendCommand1 = new Command("Ange", Command.OK, 3);
        cancelCommand1 = new Command("Bakåt", Command.BACK, 4);

        try {

            Image image3 = Image.createImage("/icon/konference24.png");

            groupList.append("Inlogg alla grupper", image3); // attribut
            groupList.append("Urlogg alla grupper", image3); // attribut
            groupList.append("Inlogg grupp", image3); // attribut
            groupList.append("Urlogg grupp", image3); // attribut
        } catch (Exception ex3) {
        }

        //groupList.addCommand(sendCommand1);
        groupList.addCommand(cancelCommand1);
        groupList.setCommandListener(this);

        //------------- dialForm -------------------------------------

        dialForm = new Form("Ring");

        dialTextField = new TextField("Ange nummer: ", "", 35, TextField.PHONENUMBER);

        DialCommand = new Command("Ring (via PBX)", Command.OK, 1);
        dialBackCommand = new Command("Bakåt", Command.BACK, 2);

        dialForm.addCommand(DialCommand);
        dialForm.addCommand(dialBackCommand);
        dialForm.setCommandListener(this);


        //--------- Fristående kommandon ---------------------------------------

        cmdBack = new Command("Bakåt", Command.BACK, 2);
        cmdDial = new Command("Ring (via PBX)", Command.OK, 1);
        emptyCallListCommand = new Command("Radera alla", Command.OK, 1);

        cmdShortDial = new Command("Ring (via PBX)", Command.OK, 1);
        cmdShortAddCommand = new Command("Lägg till", Command.OK, 2);
        shortNumbersEditCommand = new Command("Redigera", Command.OK, 3);
        DELETECommand = new Command("Radera", Command.OK, 4);
        emptyShortCallListCommand = new Command("Radera alla", Command.OK, 5);
        cmdShortBack = new Command("Bakåt", Command.BACK, 6);

        cmdShortAdd = new Command("Spara", Command.OK, 1);
        cmdShortCancel = new Command("Avbryt", Command.BACK, 2);

        goToBackInfoCommand = new Command("Bakåt", Command.BACK, 6);
        goBackCommand = new Command("Bakåt", Command.BACK, 0);

        //----- aktivera demo eller full licens ------------------------

        /*controllString();
        controllDate();
        this.ViewDateString = setViewDateString();*/

         if (ViewDateString == null) {
             this.ViewDateString = "Enterprise License";

         }

    }

    public void sendRequest(String message) {
       this.request = message;
       new Thread() {
           public void run() {
               sendMessage();
           }
       }.start();
   }

   public void sendMessage() {
       try {
           StreamConnection conn = (StreamConnection) Connector.open(url);
           OutputStream out = conn.openOutputStream();
           byte[] buf = request.getBytes();
           out.write(buf, 0, buf.length);
           out.flush();
           out.close();

           byte[] data = new byte[256];
           InputStream in = conn.openInputStream();
           int actualLength = in.read(data);
           String response = new String(data, 0, actualLength);
           setAlertMEXONOFF(response);
           in.close();
           conn.close();
       } catch (IOException ioe) {
           ioe.printStackTrace();

       }
   }
   public void setAlertMEXONOFF(String resp) {

       this.ResponceMessage = resp;

       String controll = this.ResponceMessage.substring(0, 2);

       if (controll.equals("sN")) { // Redan på

           String setStringResponse = this.ResponceMessage.substring(5);

           setStringResponse.length();

           alertDebugNOK.setString(setStringResponse);

           Display.getDisplay(this).setCurrent(alertDebugNOK);

       }

       if (controll.equals("eN")) { // Redan avstängd

           String setStringResponse = this.ResponceMessage.substring(5);

           setStringResponse.length();

           alertDebugNOK.setString(setStringResponse);

           Display.getDisplay(this).setCurrent(alertDebugNOK);

       }


       if (controll.equals("sO")) { // Mex on

           alertON.setString("Mex Started!");

           Display.getDisplay(this).setCurrent(alertON);

       }

       if (controll.equals("eO")) { // Mex off

           alertOFF.setString("Mex Stopped!");

           Display.getDisplay(this).setCurrent(alertOFF);

       }
       if (controll.equals("fO")) { // Mex on

           alertExit.setString("OBS. Mex AKTIVERAD!\nVill du avsluta?\nJa eller Nej");

           Display.getDisplay(this).setCurrent(alertExit);


       }

       if (controll.equals("fF")) { // Mex off

           alertExit.setString("OBS. Mex AVAKTIVERAD!\nVill du avsluta?\nJa eller Nej");

           Display.getDisplay(this).setCurrent(alertExit);

       }


   }


    public Form getDialForm(){

        dialForm.deleteAll();
        dialForm.append(dialTextField);

        return dialForm;
    }


    public Alert getAlertDeleteAllShortList() {

        return alertDeleteShortListAllInfo;
    }


    public Alert getAlertDeleteAll() {

       return alertDeleteCallListAllInfo;
   }


    public Alert getAlertExit() {

        return alertExit;
    }

    public Form getEditShortNumbersForm() {

        editShortNumbersForm.deleteAll();

        editShortNumbersForm.append(editShortNumberTextFieldName);
        editShortNumbersForm.append(editShortNumberTextFieldSurname);
        editShortNumbersForm.append(editShortNumberTextFieldExtension);

        return editShortNumbersForm;

    }

    public Form getTransferCallForwardForm() {

        transferForwardCallForm.deleteAll();
        transferForwardCallForm.append(ownNumberTextField);
        transferForwardCallForm.append(newNumberTextField);

        return transferForwardCallForm;
    }

    public List getListElement() {

        return referenceList;
    }

    public List getGroupList() {

        return groupList;
    }

    public Form getAbortTransferCallForm() {

        aborttransfercallForm.deleteAll();
        aborttransfercallForm.append(aborttransfercallTextField);

        return aborttransfercallForm;
    }

    public Form getCallForwardPresentForm() {

        callforwardpresentForm.deleteAll();
        callforwardpresentForm.append(callforwardpresentTextField);

        return callforwardpresentForm;
    }

    public Form getCourseForm() {

        courseForm.deleteAll();
        courseForm.append(courseTextField);

        return courseForm;
    }

    public Form getVacationForm() {

        vacationForm.deleteAll();
        vacationForm.append(vacationTextField);

        return vacationForm;
    }

    public Form getSickForm() {

        sickForm.deleteAll();
        sickForm.append(sickTextField);

        return sickForm;
    }

    public Form getTravelForm() {

        travelForm.deleteAll();
        travelForm.append(travelTextField);

        return travelForm;
    }

    public Form getFreeTimeForm(){

        freeTimeForm.deleteAll();
        freeTimeForm.append(freeTimeTextField);

        return freeTimeForm;
    }

    public Form getBusyForm() {

        busyForm.deleteAll();
        busyForm.append(outTextField);

        return busyForm;
    }

    public Form getMeetingForm() {

        meetingForm.deleteAll();
        meetingForm.append(meetingTextField);

        return meetingForm;
    }

    public Form getLunchForm() {

        lunchForm.deleteAll();
        lunchForm.append(lunchTextField);

        return lunchForm;
    }

    public Form getLogInForm() {

        loginForm.deleteAll();
        loginForm.append(loginTextField);

        return loginForm;
    }

    public Form getLogOutForm() {

        logoutForm.deleteAll();
        logoutForm.append(logoutTextField);

        return logoutForm;
    }

    public Form getAddShortForm() {

        return addShortForm;
    }

    public void setLogInStatus() {

        String logIn = loginTextField.getString();
        String loginA = "*28*";
        String loginB = logIn;
        String loginC = "#";

        String loginD = loginA + loginB + loginC;

        this.stringTotal = loginD;

        accessCode = accessNumber;

    }

    public void setLogOutStatus() {

        String logOut = logoutTextField.getString();
        String logOutA = "#28*";
        String logOutB = logOut;
        String logOutC = "#";

        String logOutD = logOutA + logOutB + logOutC;

        this.stringTotal = logOutD;

        accessCode = accessNumber;
    }

    public void setStatus(String attribut, String getInPutString) {

        String sendString = getInPutString;

        String setSend = attribut;
        String send = "";

        String one = "1";
        String two = "2";
        String three = "3";
        String four = "4";
        String five = "5";
        String six = "6";
        String seven = "7";
        String nine = "9";

        if (setSend.equals(one)) {

            send = setSend;
        }
        if (setSend.equals(two)) {

            send = setSend;
        }
        if (setSend.equals(three)) {

            send = setSend;
        }
        if (setSend.equals(four)) {

            send = setSend;
        }
        if (setSend.equals(five)) {

            send = setSend;
        }
        if (setSend.equals(six)) {

            send = setSend;
        }
        if (setSend.equals(seven)) {

            send = setSend;
        }
        if (setSend.equals(nine)) {

            send = setSend;
        }


        String C = "";
        C = sendString;
        String A = "*23*";
        String B = "#";

        String D = A + send + "*" + C + B;

        this.stringTotal = D;

        accessCode = accessNumber;

    }

    public String toString(String b) {

        String s = b;

        return s;
    }

    public void startApp() {

        try {
            setDataStore();
        } catch (InvalidRecordIDException ex) {
        } catch (RecordStoreNotOpenException ex) {
        } catch (RecordStoreException ex) {
        }
        try {
            upDateDataStore();
        } catch (RecordStoreNotOpenException ex1) {
        } catch (InvalidRecordIDException ex1) {
        } catch (RecordStoreException ex1) {
        }
        try {
            controllString();
        } catch (InvalidRecordIDException ex2) {
        } catch (RecordStoreNotOpenException ex2) {
        } catch (RecordStoreException ex2) {
        }

        Display.getDisplay(this).setCurrent(mainList);
    }

    public void pauseApp() {

    }

    public void destroyApp(boolean unconditional) {

    }

    public void checkCountryNumber() { // Justerar landsiffra som är inmatad! Tar bort '+' och lägger in '00' före landssiffran

        String larmNummer = "112";
        String Number = "+";
        String setNumber = "00";
        String validate = dialTextField.getString();
        String validate46 = "46";
        String setNumberNoll = "0";

        if (Number.equals(validate.substring(0, 1)) &&
            validate46.equals(validate.substring(1, 3))) { // Om numret startar med '+' OCH '46' är sann så gör om till '0'

            accessCode = accessNumber;

            System.out.println("+46 är SANN gör om till 0 ");

            String setString = dialTextField.getString();

            String deletePartOfString = setString.substring(3); // ta bort plast 0 - 1 ur strängen....

            String setStringTotal = setNumberNoll + deletePartOfString; // sätt ihop strängen setStringTotal

            stringTotal = setStringTotal;

            this.stringTotal = stringTotal;

            System.out.println("Landsnummer är : " + stringTotal);

        }
        if (Number.equals(validate.substring(0, 1)) &&
            !validate46.equals(validate.substring(1, 3))) { // Om numret startar med '+' OCH 46 är falsk så gör om till '00'

            accessCode = accessNumber;

            System.out.println("Andra landsnummer tex +47 blir 00 SANN");

            String setString = dialTextField.getString();

            String deletePartOfString = setString.substring(1); // ta bort plast 0 - 1 ur strängen....

            String setStringTotal = setNumber + deletePartOfString; // sätt ihop strängen setStringTotal

            stringTotal = setStringTotal;

            this.stringTotal = stringTotal;

            System.out.println("Landsnummer: " + stringTotal);

        }
        if (!Number.equals(validate.substring(0, 1))) { // ring vanligt nummer

            accessCode = accessNumber;

            this.stringTotal = dialTextField.getString();

            System.out.println("Telefonnummer: " + stringTotal);

        }
        if (validate.equals(validate.substring(0, 1)) ||
            validate.equals(validate.substring(0, 2)) ||
            validate.equals(validate.substring(0, 3)) ||
            validate.equals(validate.substring(0, 4)) ||
            validate.equals(validate.substring(0, 5))) {

            accessCode = "";

            this.stringTotal = dialTextField.getString();

            System.out.println("Internnummer: " + stringTotal);
        }
    }

    public void commandAction(Command c, Displayable d) { // SÄTTER COMMAND-ACTION STARTAR TRÄDETS KOMMANDON (trådar)
        Thread th = new Thread(this);
        thCmd = c;
        th.start();

        if (d.equals(mainList)) {
            if (c == List.SELECT_COMMAND) {
                if (d.equals(mainList)) {
                    switch (((List) d).getSelectedIndex()) {


                    case 0: // Hänvisa

                        Display.getDisplay(this).setCurrent(referenceList);

                        break;

                    case 1: // Grupper

                       // Display.getDisplay(this).setCurrent(groupList);

                        break;

                    case 2: // Vidarekoppla

                       // Display.getDisplay(this).setCurrent(callForwardList);

                        break;

                    case 3: // röstbrevlåda

                       // Display.getDisplay(this).setCurrent(voiceMailList);

                        break
                                ;

                  case 4: // Mex on

                        Thread a = new Thread() {

                            public void run() {

                            try {
                                String start_mexon_01 = "s," +
                                        switchBoardNumber + "," +
                                        accessNumber + ","
                                        + "46," /*+ voiceMessage*/ + voicemailNumber + ",";

                                sendRequest(start_mexon_01);

                                System.out.println(
                                        "Auto access 3 >> " +
                                        start_mexon_01);
                            } catch (Exception ex) {
                            }


                            }
                        };
                          a.start();

                      break
                                ;

                    case 5: // Mex off

                        Thread b = new Thread() {


                         public void run() {


                             try {
                                 String close_mexoff = "e" + ",";

                                 sendRequest(close_mexoff);

                                 System.out.println(
                                         "Auto access 3 >> " +
                                         close_mexoff);
                             } catch (Exception ex) {
                             }
                         }
                     };
                     b.start();


                        break
                                ;

                    case 6: // minimera

                        Thread minimise = new Thread() {


                         public void run() {

                             try {
                                 String close_mexoff = "m,";

                                 sendRequest(close_mexoff);

                                 System.out.println(
                                         "Auto access 3 >> " +
                                         close_mexoff);
                             } catch (Exception ex) {
                             }

                         }
                     };
                     minimise.start();


                        break;

                    }
                }

            }

        }

        if (d.equals(voiceMailList)) {
            if (c == List.SELECT_COMMAND) {
                if (d.equals(voiceMailList)) {
                    switch (((List) d).getSelectedIndex()) {

                    case 0: // Beställa

                        try {
                            String voicePost = "*21*";
                            this.accessCode = accessNumber;
                            this.stringTotal = voicePost + voicemailNumber +
                                               "#";

                            platformRequest("tel:" + switchBoardNumber + setP +
                                            stringTotal.trim()); // dial the number > DTMF-signals.

                        } catch (Exception e) {}

                        break
                                ;

                    case 1: // Avbeställa

                        try {
                            String voicePost = "#21#";
                            this.accessCode = accessNumber;
                            this.stringTotal = voicePost;

                            platformRequest("tel:" + switchBoardNumber + setP +
                                            stringTotal.trim()); // dial the number > DTMF-signals.

                        } catch (Exception e) {}

                        break;

                    case 2: // Lyssna

                        try {
                            String voicePost = "*59#";
                            this.accessCode = accessNumber;
                            this.stringTotal = voicePost;

                            platformRequest("tel:" + switchBoardNumber + setP +
                                            stringTotal.trim()); // dial the number > DTMF-signals.

                        } catch (Exception e) {}

                        break;

                    }
                }

            }

        }

        if (d.equals(referenceList)) {
            if (c == List.SELECT_COMMAND) {
                if (d.equals(referenceList)) {
                    switch (((List) d).getSelectedIndex()) {
                    case 0:// Ta bort hänvisning --> #23#

                        Thread a_absentRemove = new Thread() {

                            public void run() {

                                String deleteStatus = "#23#";
                                stringTotal = deleteStatus;

                                String absent_remove = "h," + switchBoardNumber +
                                        "," + stringTotal + ",";

                                sendRequest(absent_remove);

                                System.out.println("Remove Presence >> " + absent_remove);

                            }
                        };
                        a_absentRemove.start();

                        break;

                    case 1: // Lunch åter --> *23*0#

                        Thread a_lunch = new Thread() {

                            public void run() {

                                String deleteStatus = "*23*0#";
                                stringTotal = deleteStatus;

                                String absent_remove = "h," + switchBoardNumber +
                                        "," + stringTotal + ",";

                                sendRequest(absent_remove);

                                System.out.println("Lunch Presence >> " + absent_remove);

                            }
                        };
                        a_lunch.start();

                        break;
                    case 2: // Upptagen till --> *23*1*TIME#


                        Display.getDisplay(this).setCurrent(getBusyForm());

                        break;

                    case 3: // Frånvaro --> *23*2#

                        Thread a_away = new Thread() {

                            public void run() {

                                String deleteStatus = "*23*2#";
                                stringTotal = deleteStatus;

                                String absent_remove = "h," + switchBoardNumber +
                                        "," + stringTotal + ",";

                                sendRequest(absent_remove);

                                System.out.println("Frånvaro >> " + absent_remove);

                            }
                        };
                        a_away.start();

                        break;
                    case 4: // Möte tillbaka --> *23*3*TIME#

                        Display.getDisplay(this).setCurrent(getMeetingForm());

                        break;
                    case 5: // Tjänsteresa --> *23*4*DATE#

                        Display.getDisplay(this).setCurrent(getTravelForm());

                        break;
                    case 6: // Kurs tillbaka --> *23*5*DATE#

                        Display.getDisplay(this).setCurrent(getCourseForm());

                        break;

                    case 7: // Semester tillbaka --> *23*6*DATE#

                        Display.getDisplay(this).setCurrent(getVacationForm());

                        break
                                ;
                    case 8: // Ledig --> *23*7*DATE#

                        Display.getDisplay(this).setCurrent(getFreeTimeForm());

                        break
                                ;
                    case 9: // Gått för dagen --> *23*8#

                        Thread a_gone = new Thread() {

                            public void run() {

                                String deleteStatus = "*23*8#";
                                stringTotal = deleteStatus;

                                String absent_remove = "h," + switchBoardNumber +
                                        "," + stringTotal + ",";

                                sendRequest(absent_remove);

                                System.out.println("Gått för dagen >> " +
                                        absent_remove);

                            }
                        };
                        a_gone.start();

                    break;

                    case 10: // Sjuk --> *23*9*DATE#

                    Display.getDisplay(this).setCurrent(getSickForm());

                    break;

                    }
                }

            }

        }

        if (d.equals(callForwardList)) {
            if (c == List.SELECT_COMMAND) {
                if (d.equals(callForwardList)) {
                    switch (((List) d).getSelectedIndex()) {

                    case 0:

                        try {
                            String voicePost = "*21#";
                            this.accessCode = accessNumber;
                            this.stringTotal = voicePost;

                            platformRequest("tel:" + switchBoardNumber + setP +
                                            stringTotal.trim()); // dial the number > DTMF-signals.

                        } catch (Exception e) {}

                        break
                                ;
                    case 1:

                        Display.getDisplay(this).setCurrent(getCallForwardPresentForm());

                        break
                                ;

                    case 2:

                        Display.getDisplay(this).setCurrent(
                                getTransferCallForwardForm());

                        break
                                ;

                    case 3:

                        Display.getDisplay(this).setCurrent(
                                getAbortTransferCallForm());

                        break
                                ;

                    case 4:
                        try {
                            String deleteStatus = "#21#";
                            this.accessCode = accessNumber;
                            this.stringTotal = deleteStatus;

                            platformRequest("tel:" + switchBoardNumber + setP +
                                            stringTotal.trim());

                        } catch (Exception e) {}

                        break
                                ;

                    }
                }

            }

        }

        if (d.equals(groupList)) {
            if (c == List.SELECT_COMMAND) {
                if (d.equals(groupList)) {
                    switch (((List) d).getSelectedIndex()) {
                    case 0:

                        try {
                            String logInGroup = "*28**#";
                            this.accessCode = accessNumber;
                            this.stringTotal = logInGroup;

                            platformRequest("tel:" + switchBoardNumber + setP +
                                            stringTotal.trim()); // dial the number > DTMF-signals.

                        } catch (Exception e) {}

                        break
                                ;

                    case 1:

                        try {
                            String logOutGroup = "#28**#";
                            this.accessCode = accessNumber;
                            this.stringTotal = logOutGroup;

                            platformRequest("tel:" + switchBoardNumber + setP +
                                            stringTotal.trim()); // dial the number > DTMF-signals.

                        } catch (Exception e) {}

                        break
                                ;

                    case 2:

                        Display.getDisplay(this).setCurrent(getLogInForm());

                        break;

                    case 3:

                        Display.getDisplay(this).setCurrent(getLogOutForm());

                        break;

                    }
                }

            }

        }

    }

    public void run() {
        try {
            if (thCmd.getCommandType() == Command.EXIT) {
                notifyDestroyed();
            } else if (thCmd == lunchBackCommand) { // Kommandot 'Om Tv-Moble' hör till huvudfönstret listan

                Display.getDisplay(this).setCurrent(referenceList);
                /*ownNumberTextField, newNumberTextField;*/

            } else if (thCmd == abortTransferCallForwardDialCommand) {

                try {
                    String internForward = "#21*";
                    this.accessCode = accessNumber;
                    this.stringTotal = internForward
                                       +
                                       aborttransfercallTextField.
                                       getString() +
                                       "#";

                    platformRequest("tel:" + switchBoardNumber + setP +
                                    stringTotal.trim()); // dial the number > DTMF-signals.

                } catch (Exception e) {}

                aborttransfercallTextField.setString("");

            } else if (thCmd == transferCallForwardDialCommand) {

                try {
                    String internForward = "*21*";
                    this.accessCode = accessNumber;
                    this.stringTotal = internForward +
                                       ownNumberTextField.getString()
                                       + "*" + newNumberTextField.getString() +
                                       "#";

                    platformRequest("tel:" + switchBoardNumber + setP +
                                    stringTotal.trim()); // dial the number > DTMF-signals.

                } catch (Exception e) {}

                ownNumberTextField.setString("");
                newNumberTextField.setString("");

            } else if (thCmd == CallForwardDialCommand) {

                try {
                    String internForward = "*21*";
                    this.accessCode = accessNumber;
                    this.stringTotal = internForward +
                                       callforwardpresentTextField.getString() +
                                       "#";

                    platformRequest("tel:" + switchBoardNumber + setP +
                                    stringTotal.trim()); // dial the number > DTMF-signals.

                } catch (Exception e) {}

                callforwardpresentTextField.setString("");

            }else if (thCmd == dialBackCommand) {

                Display.getDisplay(this).setCurrent(mainList);

            } else if (thCmd == confirmExitYESCommand) {

                notifyDestroyed();

            } else if (thCmd == confirmExitNOCommand) {

                Display.getDisplay(this).setCurrent(mainList);

            } else if (thCmd == ExitCommandMainList) {

                Thread alertEND = new Thread() {

                    public void run() {

                        try {
                            String alertEND = "f,";

                            sendRequest(alertEND);

                            System.out.println("Alert END >> " + alertEND);
                        } catch (Exception ex) {
                        }

                    }
                };
                alertEND.start();


            } else if (thCmd == shortNumbersAddCommand) {

                checkSaveEditInPutShortNumber();

            } else if (thCmd == shortNumbersBackCommand) {

                genShortNumberScr("Ring anknytning", null, null, true);

            } else if (thCmd == shortNumbersCancelCommand) {

                Display.getDisplay(this).setCurrent(mainList);

            } else if (thCmd == shortNumbersEditCommand) {

                editRecordsOfShortnumbers();

            } else if (thCmd == abortTransferCallForwardBackCommand) {

                Display.getDisplay(this).setCurrent(callForwardList);

            } else if (thCmd == backShortFormCommand) {

                Display.getDisplay(this).setCurrent(mainList);

            } else if (thCmd == callForwardListBackCommand) {

                Display.getDisplay(this).setCurrent(mainList);

            } else if (thCmd == voiceBackCommand) {

                Display.getDisplay(this).setCurrent(mainList);

            } else if (thCmd == transferCallForwardCancelCommand) {

                Display.getDisplay(this).setCurrent(callForwardList);

            } else if (thCmd == CallForwardBackCommand) {

                Display.getDisplay(this).setCurrent(callForwardList);

            } else if (thCmd == cmdBack) { // Kommandot 'Redigera' hör till setting-Form

                Display.getDisplay(this).setCurrent(mainList);

            } else if (thCmd == cancelShortFormCommand) {

                Display.getDisplay(this).setCurrent(mainList);

            } else if (thCmd == addShortFormCommand) {

                genShortScr();

            } else if (thCmd == cmdShortAdd) {

                checkInPutShortNumber();

            }else if (thCmd == confirmDeleteShortListYESCommand) { // Kommandot 'Redigera' hör till setting-Form

                Display.getDisplay(this).setCurrent(alertEditSettings, mainList);
                try {
                    deleteAllShortRecords();
                } catch (RecordStoreException ex4) {
                }

            }else if (thCmd == confirmDeleteShortListNoCommand) {

                Display.getDisplay(this).setCurrent(mainList);

            } else if (thCmd == emptyShortCallListCommand) { // Kommandot 'Redigera' hör till setting-Form

                Display.getDisplay(this).setCurrent(getAlertDeleteAllShortList());

            } else if (thCmd == cmdShortAddCommand) {

                genShortScr();

            } else if (thCmd == DELETECommand) {

                deleteShortNumbersRecord();
                Display.getDisplay(this).setCurrent(mainList);

            } else if (thCmd == cmdShortCancel) {

                Display.getDisplay(this).setCurrent(mainList);

            } else if (thCmd == cmdShortBack) {

                Display.getDisplay(this).setCurrent(mainList);

            } else if (thCmd == busyBackCommand) {

                Display.getDisplay(this).setCurrent(referenceList);

            } else if (thCmd == freeTimeBackCommand) {

                Display.getDisplay(this).setCurrent(referenceList);

            }else if (thCmd == meetingBackCommand) {

                Display.getDisplay(this).setCurrent(referenceList);

            } else if (thCmd == travelBackCommand) {

                Display.getDisplay(this).setCurrent(referenceList);

            } else if (thCmd == sickBackCommand) {

                Display.getDisplay(this).setCurrent(referenceList);

            } else if (thCmd == vacationBackCommand) {

                Display.getDisplay(this).setCurrent(referenceList);

            } else if (thCmd == listBackCommand) {

                Display.getDisplay(this).setCurrent(mainList);

            } else if (thCmd == courseBackCommand) { // Kommandot 'Om Tv-Moble' hör till huvudfönstret listan

                Display.getDisplay(this).setCurrent(referenceList);

            } else if (thCmd == logInBackCommand) { // Kommandot 'Om Tv-Moble' hör till huvudfönstret listan

                Display.getDisplay(this).setCurrent(groupList);

            } else if (thCmd == logOutBackCommand) { // Kommandot 'Om Tv-Moble' hör till huvudfönstret listan

                Display.getDisplay(this).setCurrent(groupList);

            } else if (thCmd == statusCommand) { // Kommandot 'Om Tv-Moble' hör till huvudfönstret listan

                Display.getDisplay(this).setCurrent(getListElement());

            } else if (thCmd == groupcommand) { // Kommandot 'Om Tv-Moble' hör till huvudfönstret listan

                Display.getDisplay(this).setCurrent(getGroupList());

            } else if (thCmd == cmdShortAddCommand) {

                genShortScr();

            }else if (thCmd == aboutMobismaCommand) {


                setDataStore();
                upDateDataStore();

                Displayable k = new ServerNumber(switchBoardNumber, /*, IMEI,
                                                 star,*/
                                                 accessNumber, ViewDateString);
                Display.getDisplay(this).setCurrent(k);
                k.addCommand(goBackCommand);
               // k.addCommand(urlCommand);
                k.addCommand(AboutCommand);
                k.addCommand(helpCommand);
                k.setCommandListener(this);

            } else if (thCmd == settingsCommand) { // Kommandot 'Om Tv-Moble' hör till huvudfönstret listan


                setDataStore();
                upDateDataStore();

                Displayable k = new ServerNumber(switchBoardNumber, /*, IMEI,
                                                 star,*/
                                                 accessNumber, ViewDateString);
                Display.getDisplay(this).setCurrent(k);
                k.addCommand(goBackCommand);
                k.addCommand(AboutCommand);
                k.addCommand(helpCommand);
                k.setCommandListener(this);

            } else if (thCmd == editSettingBackCommand) { // Kommandot 'Tillbaka' hör till editSetting-Form

                Display.getDisplay(this).setCurrent(mainList);


            } else if (thCmd == editSettingCancelCommand) { // Kommandot 'Logga Ut' hör till setting-Form

                Display.getDisplay(this).setCurrent(mainList);

            } else if (thCmd == helpCommand) { // Kommandot 'Om Tv-Moble' hör till huvudfönstret listan

                backCommand = new Command("Bakåt", Command.OK, 2);

                Displayable k = new HelpInfo();
                Display.getDisplay(this).setCurrent(k);
                k.addCommand(backCommand);
                k.setCommandListener(this);

            } else if (thCmd == AboutCommand) { // Kommandot 'Om Tv-Moble' hör till huvudfönstret listan

                backCommand = new Command("Bakåt", Command.OK, 2);

                Displayable k = new AboutUs();
                Display.getDisplay(this).setCurrent(k);
                k.addCommand(backCommand);
                k.setCommandListener(this);

            } else if (thCmd == backCommand) { // Kommandot 'Tillbaka' hör till about-formen


                setDataStore();
                upDateDataStore();

                Displayable k = new ServerNumber(switchBoardNumber, /*, IMEI,
                                                 star,*/
                                                 accessNumber, ViewDateString);
                Display.getDisplay(this).setCurrent(k);
                k.addCommand(goBackCommand);
                k.addCommand(AboutCommand);
                k.addCommand(helpCommand);
                k.setCommandListener(this);

            } else if (thCmd == editSettingSaveCommand) { // Kommandot 'Spara' hör till editSetting-Form

                openRecStore();
                setAccessNumber();
                setSwitchBoardNumber();
                setVoiceMailNumber();
                closeRecStore();
                upDateDataStore();
                startApp();

                Display.getDisplay(this).setCurrent(alertEditSettings,
                        mainList);

            }else if (thCmd == confirmDeleteCallListYESCommand) { // Kommandot 'Redigera' hör till setting-Form

                Display.getDisplay(this).setCurrent(alertEditSettings, mainList);
                try {
                    deleteAllRecords();
                } catch (RecordStoreException ex4) {
                }

            }else if (thCmd == goBackCommand) {

                Display.getDisplay(this).setCurrent(mainList);

            }else if (thCmd == confirmDeleteCallListNOCommand) {

                Display.getDisplay(this).setCurrent(mainList);

            } else if (thCmd == emptyCallListCommand) {

                Display.getDisplay(this).setCurrent(getAlertDeleteAll());

            } else if (thCmd == propertiesCommand) { // Kommandot 'Redigera' hör till setting-Form

                Display.getDisplay(this).setCurrent(getEditSettingForm());

            } else if (thCmd == minimazeCommand) {

                Display.getDisplay(this).setCurrent(null);

            } else if (thCmd == goToBackInfoCommand) { // Kommandot 'Tillbaka' hör till about-formen

                Display.getDisplay(this).setCurrent(mainList);
            } // "tel:+46735708606/p9" >> p910/p990/m600i/N70/N80 // tel:+46851492163;postd=9 >> k700,k750,v600,s700,w810

            else if (thCmd == cancelCommand) { // Kommandot 'Tillbaka' hör till about-formen

                Display.getDisplay(this).setCurrent(getDialForm());
            } else if (thCmd == cancelCommand1) { // Kommandot 'Tillbaka' hör till about-formen

                Display.getDisplay(this).setCurrent(mainList);

            } else if (thCmd == cmdDial) {
                // dial the phone screen
                genDialScr();

            } else if (thCmd == cmdShortDial) {
                // dial the phone screen
                genDialShortScr();

            } else if (thCmd == DialCommand) {
                if (type == 0) {
                    try {
                        checkCountryNumber();
                        if (SOS.equals(stringTotal)) {
                            platformRequest("tel:" + stringTotal.trim());
                        } else {

                            platformRequest("tel:" + switchBoardNumber + setP +
                                            accessCode + stringTotal.trim()); // dial the number > DTMF-signals.
                        }

                        String dialedNumber = stringTotal;
                        saveDialedNumber(dialedNumber);
                        dialTextField.setString("");
                        getTime();

                    } catch (Exception e) {}
                } else {
                    try {
                        platformRequest("tel:" + switchBoardNumber + setP +
                                        accessCode + stringTotal.trim()); // open the wap browser.
                    } catch (Exception e) {}
                }
            } else if (thCmd == lunchSendCommand) { //
                if (type == 0) {
                    try {

                        String sendAttLunch = lunchTextField.getString();
                        String one = "1";
                        setStatus(one, sendAttLunch);
                        platformRequest("tel:" + switchBoardNumber + setP +
                                        stringTotal.trim()); // dial the number > DTMF-signals.

                    } catch (Exception e) {}
                    lunchTextField.setString("");
                } else {
                    try {
                        platformRequest("tel:" + switchBoardNumber + setP +
                                        stringTotal.trim()); // open the wap browser.

                    } catch (Exception e) {}

                }
                lunchTextField.setString("");
            }else if (thCmd == freeTimeSendCommand) { //
                if (type == 0) {

                    Thread a_free = new Thread() {

                        String sendAttOut = freeTimeTextField.getString();


                        public void run() {

                            if(sendAttOut.equals("")){

                                getFreeTimeForm();

                            }
                            if(!sendAttOut.equals("")){

                            String two = "7";
                            setStatus(two, sendAttOut);

                            String absent_remove = "h," + switchBoardNumber +
                                    "," + stringTotal + ",";

                            sendRequest(absent_remove);

                            System.out.println("Ledig >> " + absent_remove);
                        }
                        }
                    };
                    a_free.start();
                    freeTimeTextField.setString("");

                }
            }  else if (thCmd == busySendCommand) { //
                if (type == 0) {

                    Thread a_busy = new Thread() {

                        String sendAttOut = outTextField.getString();

                        public void run() {

                            if(sendAttOut.equals("")){

                                getBusyForm();

                            }
                            if(!sendAttOut.equals("")){

                                String two = "1";
                                setStatus(two, sendAttOut);

                                String absent_remove = "h," + switchBoardNumber +
                                        "," + stringTotal + ",";

                                sendRequest(absent_remove);

                                System.out.println("Upptagen >> " +
                                        absent_remove);
                            }
                        }
                    };
                    a_busy.start();
                    outTextField.setString("");

                }
            } else if (thCmd == meetingSendCommand) { //
                if (type == 0) {

                    Thread a_meeting = new Thread() {

                        String sendAttOut = meetingTextField.getString();

                        public void run() {

                            if(sendAttOut.equals("")){

                                getMeetingForm();

                            }
                            if(!sendAttOut.equals("")){



                                String two = "3";
                                setStatus(two, sendAttOut);

                                String absent_remove = "h," + switchBoardNumber +
                                        "," + stringTotal + ",";

                                sendRequest(absent_remove);

                                System.out.println("Upptagen >> " +
                                        absent_remove);
                            }
                        }
                    };
                    a_meeting.start();
                    meetingTextField.setString("");

                }
            } else if (thCmd == travelSendCommand) { //
                if (type == 0) {

                    Thread a_travel = new Thread() {

                        String sendAttOut = travelTextField.getString();

                        public void run() {

                            if(sendAttOut.equals("")){

                                getTravelForm();

                            }
                            if(!sendAttOut.equals("")){



                                String two = "4";
                                setStatus(two, sendAttOut);

                                String absent_remove = "h," + switchBoardNumber +
                                        "," + stringTotal + ",";

                                sendRequest(absent_remove);

                                System.out.println("Tjänsteresa >> " +
                                        absent_remove);
                            }
                        }
                    };
                    a_travel.start();
                    travelTextField.setString("");


                }
            } else if (thCmd == sickSendCommand) { //
                if (type == 0) {

                    Thread a_sick = new Thread() {

                        String sendAttOut = sickTextField.getString();

                        public void run() {

                            if(sendAttOut.equals("")){

                                getSickForm();

                            }
                            if(!sendAttOut.equals("")){


                                String two = "9";
                                setStatus(two, sendAttOut);

                                String absent_remove = "h," + switchBoardNumber +
                                        "," + stringTotal + ",";

                                sendRequest(absent_remove);

                                System.out.println("Sjuk >> " + absent_remove);
                            }
                        }
                    };
                    a_sick.start();
                    sickTextField.setString("");

                }
            } else if (thCmd == vacationSendCommand) { //
                if (type == 0) {

                    Thread a_vacation = new Thread() {

                        String sendAttOut = vacationTextField.getString();

                        public void run() {

                            if(sendAttOut.equals("")){

                                getVacationForm();

                            }
                            if(!sendAttOut.equals("")){



                                String two = "6";
                                setStatus(two, sendAttOut);

                                String absent_remove = "h," + switchBoardNumber +
                                        "," + stringTotal + ",";

                                sendRequest(absent_remove);

                                System.out.println("Semester >> " +
                                        absent_remove);
                            }
                        }
                    };
                    a_vacation.start();
                    vacationTextField.setString("");

                }
            } else if (thCmd == courseSendCommand) { //
                if (type == 0) {

                    Thread a_course = new Thread() {

                        String sendAttOut = courseTextField.getString();

                        public void run() {

                            if(sendAttOut.equals("")){

                                getCourseForm();

                            }
                            if(!sendAttOut.equals("")){



                                String two = "5";
                                setStatus(two, sendAttOut);

                                String absent_remove = "h," + switchBoardNumber +
                                        "," + stringTotal + ",";

                                sendRequest(absent_remove);

                                System.out.println("Sjuk >> " + absent_remove);
                            }
                        }
                    };
                    a_course.start();
                    courseTextField.setString("");


                }
            } else if (thCmd == logInSendCommand) { //
                if (type == 0) {

                    Thread a_login = new Thread() {

                        public void run() {

                            setLogInStatus();

                            String absent_remove = "h," + switchBoardNumber +
                                    "," + stringTotal + ",";

                            sendRequest(absent_remove);

                            System.out.println("login >> " + absent_remove);

                        }
                    };
                    a_login.start();
                    loginTextField.setString("");

                }
            } else if (thCmd == logOutSendCommand) { //
                if (type == 0) {
                    Thread a_logout = new Thread() {

                        public void run() {

                            setLogInStatus();

                            String absent_remove = "h," + switchBoardNumber +
                                    "," + stringTotal + ",";

                            sendRequest(absent_remove);

                            System.out.println("login >> " + absent_remove);

                        }
                    };
                    a_logout.start();
                    logoutTextField.setString("");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("WMAMIDlet.run Exception " + e);
        }
    }

    //------------ D A T A - B A S - R M S -----------------------------------------

    public Form getEditSettingForm() { // METODEN RETURNERAR FORMEN FÖR EDITSETTINGS I EGENSKAPER

        editSettingForm.deleteAll();
        openRecStore();
        accessNumbers.setString(accessNumber);
        editSettingForm.append(accessNumbers);
        editSwitchBoardNumber.setString(switchBoardNumber);
        editSettingForm.append(editSwitchBoardNumber);
        voiceMailNumber.setString(voicemailNumber);
        editSettingForm.append(voiceMailNumber);
        closeRecStore();

        return editSettingForm;
    }

    // --- SET-metoder ------


    public void setAccessNumber() {

        try {
            recStore.setRecord(4, accessNumbers.getString().getBytes(), 0,
                               accessNumbers.getString().length());
        } catch (Exception e) {
            // ALERT
        }
    }

    public void setSwitchBoardNumber() {
        try {
            recStore.setRecord(5, editSwitchBoardNumber.getString().getBytes(),
                               0,
                               editSwitchBoardNumber.getString().length());
        } catch (Exception e) {
            // ALERT
        }
    }

    public void setVoiceMailNumber() {
        try {
            recStore.setRecord(9, voiceMailNumber.getString().getBytes(),
                               0,
                               voiceMailNumber.getString().length());
        } catch (Exception e) {
            // ALERT
        }
    }


    public void setTWO() {
        try {

            openRecStore();
            String appt = "2";
            byte bytes[] = appt.getBytes();
            recStore.addRecord(bytes, 0, bytes.length);

            closeRecStore();
            upDateDataStore();
            startApp();

        } catch (Exception e) {
            // ALERT
        }
    }


    // ---- GET-metoder ---------

    public String getYear() throws InvalidRecordIDException,
            RecordStoreNotOpenException, RecordStoreException {

        openRecStore();

        byte a[] = recStore.getRecord(1);
        setYear = new String(a, 0, a.length);

        closeRecStore();

        return setYear;

    }

    public String getMounth() throws InvalidRecordIDException,
            RecordStoreNotOpenException, RecordStoreException {

        openRecStore();

        byte b[] = recStore.getRecord(2);
        setMounth = new String(b, 0, b.length);

        closeRecStore();

        return setMounth;

    }

    public String getDate() throws InvalidRecordIDException,
            RecordStoreNotOpenException, RecordStoreException {

        openRecStore();

        byte c[] = recStore.getRecord(3);
        setDate = new String(c, 0, c.length);

        closeRecStore();

        return setDate;

    }

    public String getAccessNumber() throws InvalidRecordIDException,
            RecordStoreNotOpenException, RecordStoreException {

        openRecStore();

        byte d[] = recStore.getRecord(4);
        accessNumber = new String(d, 0, d.length);

        closeRecStore();

        return accessNumber;

    }

    public String getVoiceMailNumber() throws InvalidRecordIDException,
            RecordStoreNotOpenException, RecordStoreException {

        openRecStore();

        byte voice[] = recStore.getRecord(9);
        voicemailNumber = new String(voice, 0, voice.length);

        closeRecStore();

        return voicemailNumber;

    }


    public String getSwitchBoardNumber() throws InvalidRecordIDException,
            RecordStoreNotOpenException, RecordStoreException {

        openRecStore();

        byte e[] = recStore.getRecord(5);
        switchBoardNumber = new String(e, 0, e.length);

        closeRecStore();

        return switchBoardNumber;

    }

    public String getThisYearBack() throws InvalidRecordIDException,
            RecordStoreNotOpenException, RecordStoreException {

        openRecStore();

        byte f[] = recStore.getRecord(6);
        setyearBack = new String(f, 0, f.length);

        closeRecStore();

        return setyearBack;

    }

    public String getThisMounthBack() throws InvalidRecordIDException,
            RecordStoreNotOpenException, RecordStoreException {

        openRecStore();

        byte g[] = recStore.getRecord(7);
        setmounthBack = new String(g, 0, g.length);

        closeRecStore();

        return setmounthBack;

    }

    public String getThisDayBack() throws InvalidRecordIDException,
            RecordStoreNotOpenException, RecordStoreException {

        openRecStore();

        byte h[] = recStore.getRecord(8);
        setdayBack = new String(h, 0, h.length);

        closeRecStore();

        return setdayBack;

    }


    public void getTWO() throws InvalidRecordIDException,
            RecordStoreNotOpenException, RecordStoreException {

        openRecStore();
        readRecords();
        readRecordsUpdate();

        try {
            byte i[] = recStore.getRecord(10);
            getTWO = new String(i, 0, i.length);
        } catch (InvalidRecordIDException ex) {
        } catch (RecordStoreNotOpenException ex) {
        } catch (RecordStoreException ex) {
        }

        try {
            this.dateString = getTWO;
        } catch (Exception ex1) {
        }

        System.out.println("häääääääääärrrrrrrr >>> getTWO >> " + getTWO);
        closeRecStore();

    }

    public void readRecordsUpdate() {
        try {
            System.out.println("Number of records: " + recStore.getNumRecords());

            if (recStore.getNumRecords() > 0) {
                RecordEnumeration re = recStore.enumerateRecords(null, null, false);
                while (re.hasNextElement()) {
                    String str = new String(re.nextRecord());
                    System.out.println("Record: " + str);
                }
            }
        } catch (Exception e) {
            System.err.println(e.toString());
        }
    }

    public void readRecords() {
        try {
            // Intentionally small to test code below
            byte[] recData = new byte[5];
            int len;

            for (int i = 1; i <= recStore.getNumRecords(); i++) {
                // Allocate more storage if necessary
                if (recStore.getRecordSize(i) > recData.length) {
                    recData = new byte[recStore.getRecordSize(i)];
                }

                len = recStore.getRecord(i, recData, 0);
                if (Settings.debug) {
                    System.out.println("Record ID#" + i + ": " +
                                       new String(recData, 0, len));
                }
            }
        } catch (Exception e) {
            System.err.println(e.toString());
        }
    }

    public void writeRecord(String str) {
        byte[] rec = str.getBytes();

        try {
            System.out.println("sparar ");
            recStore.addRecord(rec, 0, rec.length);
            System.out.println("Writing record: " + str);
        } catch (Exception e) {
            System.err.println(e.toString());
        }
    }


    public void openRecStore() {
        try {
            System.out.println("Öppnar databasen");
            // The second parameter indicates that the record store
            // should be created if it does not exist
            recStore = RecordStore.openRecordStore(REC_STORE, true);

        } catch (Exception e) {
            System.err.println(e.toString());
        }
    }

    public void closeRecStore() {
        try {
            recStore.closeRecordStore();
        } catch (Exception e) {
            System.err.println(e.toString());
        }
    }

    public void setDataStore() throws RecordStoreNotOpenException,
            InvalidRecordIDException, RecordStoreNotOpenException,
            RecordStoreException {

        openRecStore();
        readRecords();
        readRecordsUpdate();

        if (recStore.getNumRecords() == 0) { // om innehållet i databasen är '0' så spara de tre första elementen i databasen.

            writeRecord(setYear);
            writeRecord(setMounth);
            writeRecord(setDate);
            writeRecord("0");
            writeRecord("+46");
            writeRecord(setyearBack);
            writeRecord(setmounthBack);
            writeRecord(setdayBack);
            writeRecord("0");

        }

        // sätter nummer i fönstret under inställningar...

        byte d[] = recStore.getRecord(4);
        accessNumber = new String(d, 0, d.length);

        byte e[] = recStore.getRecord(5);
        switchBoardNumber = new String(e, 0, e.length);

        byte voice[] = recStore.getRecord(9);
        voicemailNumber = new String(voice, 0, voice.length);

        closeRecStore();
    }

    // Om något inputfönster(post) i databasen är tom sätt tillbaka värdet...
    public void upDateDataStore() throws InvalidRecordIDException,
            RecordStoreNotOpenException, RecordStoreException {

        openRecStore();
        String setBackVoiceMailRecord = voicemailNumber;
        String setBackAccessNumberRecord = accessNumber;
        String setBackSwitchBoardNumberRecord = switchBoardNumber;

        if (recStore.getRecord(1) == null && recStore.getRecord(4) == null &&
            recStore.getRecord(5) == null) {

            recStore.setRecord(9, setBackVoiceMailRecord.getBytes(), 0,
                               setBackVoiceMailRecord.length());
            recStore.setRecord(4, setBackAccessNumberRecord.getBytes(), 0,
                               setBackAccessNumberRecord.length());
            recStore.setRecord(5, setBackSwitchBoardNumberRecord.getBytes(), 0,
                               setBackSwitchBoardNumberRecord.length());
        } else if (recStore.getRecord(9) == null && recStore.getRecord(4) == null) {

            recStore.setRecord(9, setBackVoiceMailRecord.getBytes(), 0,
                               setBackVoiceMailRecord.length());
            recStore.setRecord(4, setBackAccessNumberRecord.getBytes(), 0,
                               setBackAccessNumberRecord.length());

        } else if (recStore.getRecord(4) == null && recStore.getRecord(5) == null) {

            recStore.setRecord(4, setBackAccessNumberRecord.getBytes(), 0,
                               setBackAccessNumberRecord.length());
            recStore.setRecord(5, setBackSwitchBoardNumberRecord.getBytes(), 0,
                               setBackSwitchBoardNumberRecord.length());
        } else if (recStore.getRecord(9) == null && recStore.getRecord(5) == null) {

            recStore.setRecord(9, setBackVoiceMailRecord.getBytes(), 0,
                               setBackVoiceMailRecord.length());
            recStore.setRecord(5, setBackSwitchBoardNumberRecord.getBytes(), 0,
                               setBackSwitchBoardNumberRecord.length());
        } else if (recStore.getRecord(9) == null) {

            recStore.setRecord(9, setBackVoiceMailRecord.getBytes(), 0,
                               setBackVoiceMailRecord.length());
        } else if (recStore.getRecord(4) == null) {

            recStore.setRecord(4, setBackAccessNumberRecord.getBytes(), 0,
                               setBackAccessNumberRecord.length());
        } else if (recStore.getRecord(5) == null) {

            recStore.setRecord(5, setBackSwitchBoardNumberRecord.getBytes(), 0,
                               setBackSwitchBoardNumberRecord.length());
        }

        closeRecStore();
    }


// ------------------- D A T U M -----------------------------------------------

    public void controllString() throws RecordStoreNotOpenException,
            InvalidRecordIDException, RecordStoreException {

        String readRecord;

        getTWO();

        readRecord = dateString;

        String viewRecord = readRecord;

        try {
            if (viewRecord.equals("2")) {

                notifyDestroyed();
            }
        } catch (Exception ex) {
        }
        System.out.println("VÄRDET PLATS 9 DB >> " + viewRecord);
    }

    public void controllDate() throws RecordStoreNotOpenException,
            InvalidRecordIDException, RecordStoreException {

        try {
            this.DBdate = getDate();
        } catch (RecordStoreNotOpenException ex) {
        } catch (InvalidRecordIDException ex) {
        } catch (RecordStoreException ex) {
        }
        try {
            this.DBmounth = getMounth();
        } catch (RecordStoreNotOpenException ex1) {
        } catch (InvalidRecordIDException ex1) {
        } catch (RecordStoreException ex1) {
        }
        try {
            this.DByear = getYear();
        } catch (RecordStoreNotOpenException ex2) {
        } catch (InvalidRecordIDException ex2) {
        } catch (RecordStoreException ex2) {
        }
        try {
            this.DBdateBack = getThisDayBack();
        } catch (RecordStoreNotOpenException ex3) {
        } catch (InvalidRecordIDException ex3) {
        } catch (RecordStoreException ex3) {
        }
        try {
            this.DBmounthBack = getThisMounthBack();
        } catch (RecordStoreNotOpenException ex4) {
        } catch (InvalidRecordIDException ex4) {
        } catch (RecordStoreException ex4) {
        }
        try {
            this.DByearBack = getThisYearBack();
        } catch (RecordStoreNotOpenException ex5) {
        } catch (InvalidRecordIDException ex5) {
        } catch (RecordStoreException ex5) {
        }

        String useDBdate = DBdate.trim();
        String useDBmounth = DBmounth.trim();
        String useDByear = DByear.trim();

        String useDBdateBack = DBdateBack.trim();
        String useDBmounthBack = DBmounthBack.trim();
        String useDByearBack = DByearBack.trim();

        System.out.println("Skriver ut datum om 30 dagar >>> " + useDBdate);
        System.out.println("Skriver ut månad om 30 dagar >>> " + useDBmounth);
        System.out.println("Skriver ut året om 30 dagar >>> " + useDByear);

        System.out.println("Skriver ut Kontroll datum >>> " + useDBdateBack);
        System.out.println("Skriver ut Kontroll månad >>> " + useDBmounthBack);
        System.out.println("Skriver ut Kontroll år >>> " + useDByearBack);

        String toDayDate = checkDay().trim();
        String toDayMounth = checkMounth().trim();

        System.out.println("Skriver ut DAGENS DATUM >>> " + toDayDate);
        System.out.println("Skriver ut ÅRETS MÅNAD >>> " + toDayMounth);

        Integer controllDBdateBack = new Integer(0); // Gör om strängar till integer
        Integer controllDBmonthBack = new Integer(0); // Gör om strängar till integer
        Integer controllDByearBack = new Integer(0); // Gör om strängar till integer

        int INTDBdateBack = controllDBdateBack.parseInt(useDBdateBack);
        int INTDBmounthBack = controllDBmonthBack.parseInt(DBmounthBack);
        int INTDByearBack = controllDByearBack.parseInt(DByearBack);

        Integer controllDBdate = new Integer(0); // Gör om strängar till integer
        Integer controllDBmonth = new Integer(0); // Gör om strängar till integer
        Integer controllDByear = new Integer(0); // Gör om strängar till integer

        Integer controllToDayDBdate = new Integer(0); // Gör om strängar till integer
        Integer controllToDayDBmounth = new Integer(0); // Gör om strängar till integer

        int INTDBdate = controllDBdate.parseInt(useDBdate);
        int INTDBmounth = controllDBmonth.parseInt(DBmounth);
        int INTDByear = controllDByear.parseInt(DByear);

        int INTdateToDay = controllToDayDBdate.parseInt(toDayDate);
        int INTmounthToDay = controllToDayDBmounth.parseInt(toDayMounth);

        if (INTDBdate <= INTdateToDay && INTDBmounth <= INTmounthToDay &&
            INTDByear == checkYear) {

            System.out.println("SANN SANN SANN SANN SANN ");

            setTWO(); // Om månad och datum är sann skriv in "2" i databasen plats 6...

        }
        if (INTmounthToDay == 0) { // Om INTmounthToDay har värdet '0' som är januari

            INTDBmounthBack = 0; // Då innehåller installations-månaden samma värde som nu-månaden.

        }
        if (INTDBmounthBack > INTmounthToDay) { // Om installations-månaden är större än 'dagens' månad som är satt i mobilen så stäng...

            setTWO(); // Om månad och datum är sann skriv in "2" i databasen plats 6...

        }
        if (INTDBmounthBack > INTmounthToDay && INTDByearBack < checkYear) { // Om installations-månaden är större än 'dagens' månad som är satt i mobilen så stäng...

            setTWO(); // Om månad och datum är sann skriv in "2" i databasen plats 6...

        }
        if (INTDByearBack > checkYear) { // Om installations-året är större än året som är satt i mobilen. >> går bakåt i tiden...

            setTWO(); // Om månad och datum är sann skriv in "2" i databasen plats 6...

        }
        if (INTDBdateBack > INTdateToDay && INTDBmounthBack > INTmounthToDay &&
            INTDByearBack > checkYear) {

            setTWO(); // Om månad och datum är sann skriv in "2" i databasen plats 6...

        }
        if (INTDBmounthBack > INTmounthToDay && INTDByearBack > checkYear) {

            setTWO(); // Om månad och datum är sann skriv in "2" i databasen plats 6...

        }

    }


    public void setDBDate() throws RecordStoreNotOpenException,
            InvalidRecordIDException, RecordStoreException {

        countDay();

        System.out.println("Om 30 dagar är det den >> " + dayAfter +
                           ", och månad >> " + monthAfter + " det är år >> " +
                           yearAfter);

        String convertDayAfter = Integer.toString(dayAfter); // konvertera int till string...
        String convertMounthAfter = Integer.toString(monthAfter);
        String convertYearAfter = Integer.toString(yearAfter);

        this.setDate = convertDayAfter;
        this.setMounth = convertMounthAfter;
        this.setYear = convertYearAfter;

    }

    public void setDBDateBack() {

        countThisDay();

        System.out.println("Kontrollerar dagens dautm >> " + dayBack +
                           ", och månad >> " + mounthBack + " det är år >> " +
                           yearBack);

        String convertDayBack = Integer.toString(dayBack); // konvertera int till string...
        String convertMounthBack = Integer.toString(mounthBack);
        String convertYearBack = Integer.toString(yearBack);

        this.setdayBack = convertDayBack;
        this.setmounthBack = convertMounthBack;
        this.setyearBack = convertYearBack;

    }

    public void countThisDay() {

        // Get today's day and month
        Calendar cal = Calendar.getInstance();
        Date date = new Date();
        cal.setTime(date);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int year = cal.get(Calendar.YEAR);
        System.out.println("Dagens datum är den >> " + day +
                           ", Årets månad är nummer >> " + month +
                           " det är år >> " + year);

        this.dayBack = day;
        this.mounthBack = month;
        this.yearBack = year;

    }

    public void countDay() {

        // Get today's day and month
        Calendar cal = Calendar.getInstance();
        Date date = new Date();
        cal.setTime(date);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int year = cal.get(Calendar.YEAR);
        System.out.println("Dagens datum är den >> " + day +
                           ", Årets månad är nummer >> " + month +
                           " det är år >> " + year);
        this.checkYear = year;

        // Räknar fram 30 dagar framåt vilket datum år osv...
        final long MILLIS_PER_DAY = 24 * 60 * 60 * 1000L;
        long offset = date.getTime();
        offset += antalDagar * MILLIS_PER_DAY;
        date.setTime(offset);
        cal.setTime(date);

        // Now get the adjusted date back
        month = cal.get(Calendar.MONTH);
        day = cal.get(Calendar.DAY_OF_MONTH);
        year = cal.get(Calendar.YEAR);

        this.dayAfter = day;
        this.monthAfter = month;
        this.yearAfter = year;

    }

    private String regFromTextFile() { // Läser textfilen tmp.txt
        InputStream is = getClass().getResourceAsStream("tmp.txt");
        try {
            StringBuffer sb = new StringBuffer();
            int chr, i = 0;
            // Read until the end of the stream
            while ((chr = is.read()) != -1) {
                sb.append((char) chr);
            }

            return sb.toString();
        } catch (Exception e) {
            System.out.println("Unable to create stream");
        }
        return null;
    }

    public String setViewDateString() throws InvalidRecordIDException,
            RecordStoreNotOpenException, RecordStoreException {

        //ViewDateString

        String e1 = getDate();
        String e2 = setMounth();
        String e3 = getYear();

        ViewDateString = e1 + " " + e2 + " " + e3;

        return ViewDateString;

    }

    public String setMounth() throws RecordStoreNotOpenException,
            InvalidRecordIDException, RecordStoreException {

        setViewMounth = getMounth();

        if (setViewMounth.equals("0")) {

            this.setViewMounth = "Januari";
        }
        if (setViewMounth.equals("1")) {

            this.setViewMounth = "Februari";
        }
        if (setViewMounth.equals("2")) {

            this.setViewMounth = "Mars";
        }
        if (setViewMounth.equals("3")) {

            this.setViewMounth = "April";
        }
        if (setViewMounth.equals("4")) {

            this.setViewMounth = "Maj";
        }
        if (setViewMounth.equals("5")) {

            this.setViewMounth = "Juni";
        }
        if (setViewMounth.equals("6")) {

            this.setViewMounth = "Juli";
        }
        if (setViewMounth.equals("7")) {

            this.setViewMounth = "Augusti";
        }
        if (setViewMounth.equals("8")) {

            this.setViewMounth = "September";
        }
        if (setViewMounth.equals("9")) {

            this.setViewMounth = "Oktober";
        }
        if (setViewMounth.equals("10")) {

            this.setViewMounth = "November";
        }
        if (setViewMounth.equals("11")) {

            this.setViewMounth = "December";
        }

        String viewMounth = setViewMounth;

        return viewMounth;
    }

    public String checkDay() {

        String mobileClock = today.toString(); // Tilldelar mobileClock 'todays' datumvärde, skickar och gör om till en string av java.lang.string-typ

        String checkDayString = mobileClock.substring(8, 10); // plockar ut 'datum' tecken ur klockan

        if (checkDayString.equals("01")) {

            checkDayString = "1";

        } else if (checkDayString.equals("02")) {

            checkDayString = "2";

        } else if (checkDayString.equals("03")) {

            checkDayString = "3";

        } else if (checkDayString.equals("04")) {

            checkDayString = "4";

        } else if (checkDayString.equals("05")) {

            checkDayString = "5";

        } else if (checkDayString.equals("06")) {

            checkDayString = "6";

        } else if (checkDayString.equals("07")) {

            checkDayString = "7";

        } else if (checkDayString.equals("08")) {

            checkDayString = "8";

        } else if (checkDayString.equals("09")) {

            checkDayString = "9";

        }

        String useStringDate = checkDayString;

        return useStringDate;

    }

    public String checkMounth() {

        String mobileClock = today.toString(); // Tilldelar mobileClock 'todays' datumvärde, skickar och gör om till en string av java.lang.string-typ

        String checkMounthString = mobileClock.substring(4, 7); // plockar ut 'Månad' tecken ur klockan

        if (checkMounthString.equals("Jan")) {

            checkMounthString = "0";

        } else if (checkMounthString.equals("Feb")) {

            checkMounthString = "1";

        } else if (checkMounthString.equals("Mar")) {

            checkMounthString = "2";

        } else if (checkMounthString.equals("Apr")) {

            checkMounthString = "3";

        } else if (checkMounthString.equals("May")) {

            checkMounthString = "4";

        } else if (checkMounthString.equals("Jun")) {

            checkMounthString = "5";

        } else if (checkMounthString.equals("Jul")) {

            checkMounthString = "6";

        } else if (checkMounthString.equals("Aug")) {

            checkMounthString = "7";

        } else if (checkMounthString.equals("Sep")) {

            checkMounthString = "8";

        } else if (checkMounthString.equals("Oct")) {

            checkMounthString = "9";

        } else if (checkMounthString.equals("Nov")) {

            checkMounthString = "10";

        } else if (checkMounthString.equals("Dec")) {

            checkMounthString = "11";

        }

        String useStringMounth = checkMounthString;

        return useStringMounth;

    }

    public void deleteAllRecords() throws RecordStoreException {

        RecordStore rs = null;
        RecordEnumeration re = null;
        try {
            rs = RecordStore.openRecordStore("TheAddressBook", true);
            re = rs.enumerateRecords(null, null, false);

            // First remove all records, a little clumsy.
            while (re.hasNextElement()) {
                int id = re.nextRecordId();
                rs.deleteRecord(id);
            }
        } catch (Exception ex) {
        }

    }


    private void displayAlert(int type, String msg, Screen s) {
        alert.setString(msg);

        switch (type) {
        case ERROR:
            alert.setTitle("Error!");
            alert.setType(AlertType.ERROR);
            break;
        case INFO:
            alert.setTitle("Info");
            alert.setType(AlertType.INFO);
            break;

        case SIZE:
            alert.setTitle("MaxSize!");
            alert.setType(AlertType.ERROR);
            break;

        case INPUT:
            alert.setTitle("Error!");
            alert.setType(AlertType.ERROR);
            break;

        case NAMEERROR:
            alert.setTitle("Error!");
            alert.setType(AlertType.ERROR);
            break;

        }
        Display.getDisplay(this).setCurrent(alert,
                                            s == null ? display.getCurrent() :
                                            s);

    }


    private Screen genNameScr(String title, String f, String l, boolean local) {
        SimpleComparator sc;
        SimpleFilter sf = null;
        RecordEnumeration re;
        phoneNums = null;

        if (local) {
            sc = new SimpleComparator(
                    sortOrder == 0 ? SimpleComparator.SORT_BY_FIRST_NAME
                    : SimpleComparator.SORT_BY_LAST_NAME);

            try {
                re = addrBook.enumerateRecords(sf, sc, false);
            } catch (Exception e) {
                displayAlert(ERROR, "Could not create enumeration: " + e, null);
                return null;
            }
        } else {
            re = null;
        }

        nameList = null;
        if (re.hasNextElement()) {
            nameList = new List(title, List.IMPLICIT);
            nameList.addCommand(cmdBack);
            nameList.addCommand(cmdDial);
            nameList.addCommand(emptyCallListCommand);
            nameList.setCommandListener(this);
            phoneNums = new Vector(6);

            try {
                re = addrBook.enumerateRecords(null, null, false);
                while (re.hasNextElement()) {

                    byte[] b = re.nextRecord();

                    String pn = SimpleRecord.getPhoneNum(b);
                    nameList.append(SimpleRecord.getPhoneNum(b) + " " +
                                    SimpleRecord.getLastName(b) + " " +
                                    SimpleRecord.getFirstName(b), null);

                    System.out.println("Record: " + pn);
                    phoneNums.addElement(pn);

                }
            } catch (Exception e) {
                displayAlert(ERROR, "Error while building name list: " + e,
                             null);
                return null;
            }
            Display.getDisplay(this).setCurrent(nameList);
        } else {
            displayAlert(INFO, "Samtalslistan är tom!", mainList);
        }

        return nameList;
    }

    public String checkValidateNumber(String s) {

        String validateNumber = s;

        if (validateNumber.equals(validateNumber.substring(0, 1)) ||
            validateNumber.equals(validateNumber.substring(0, 2)) ||
            validateNumber.equals(validateNumber.substring(0, 3)) ||
            validateNumber.equals(validateNumber.substring(0, 4))) {

            accessNumber = "";

            System.out.println("Internnummer: " + validateNumber);
        } else {
            try {
                accessNumber = getAccessNumber();
            } catch (RecordStoreNotOpenException ex) {
            } catch (InvalidRecordIDException ex) {
            } catch (RecordStoreException ex) {
            }
        }

        return validateNumber;
    }

    private void genDialScr() throws RecordStoreNotOpenException,
            InvalidRecordIDException, RecordStoreException {

        String validateNumber = (String) phoneNums.elementAt(nameList.
                getSelectedIndex());
        validateNumber = checkValidateNumber(validateNumber);

        try {
            platformRequest("tel:" + switchBoardNumber + setP +
                            accessNumber +
                            validateNumber); // dial the number > DTMF-signals.
        } catch (ConnectionNotFoundException ex1) {
        }

    }


    public String getTime() {

        String stringMinutes;

        Calendar cal = Calendar.getInstance();
        Date date = new Date();
        cal.setTime(date);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);
        int year = cal.get(Calendar.YEAR);
        int mounth = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);

        String sub = year + "";

        String years = sub.substring(2);

        if (minute == 0 || minute == 1 || minute == 2 || minute == 3 ||
            minute == 4
            || minute == 5 || minute == 6 || minute == 7
            || minute == 8 || minute == 9) {

            stringMinutes = Integer.toString(minute);

            String subMinutes = "0";

            stringMinutes = subMinutes + stringMinutes;

            String time = hour + "." + stringMinutes + " " + day + "/" + mounth +
                          "-" + years;

            System.out.println("klockan är >> " + time);

            return time;

        }

        String time = hour + "." + minute + " " + day + "/" + mounth + "-" +
                      years;

        System.out.println("klockan är >> " + time);

        return time;

    }

    private Screen genShortNumberScr(String title, String f, String l,
                                     boolean local) {
        SimpleComparator sc;
        SimpleFilter sf = null;
        RecordEnumeration re;
        shortNums = null;

        if (local) {
            sc = new SimpleComparator(
                    sortOrder == 0 ? SimpleComparator.SORT_BY_FIRST_NAME
                    : SimpleComparator.SORT_BY_LAST_NAME);

            try {
                re = shortNumbers.enumerateRecords(sf, sc, false);
            } catch (Exception e) {
                displayAlert(ERROR, "Could not create enumeration: " + e, null);
                return null;
            }
        } else {
            re = null;
        }

        shortList = null;
        if (re.hasNextElement()) {
            shortList = new List(title, List.IMPLICIT);
            shortList.addCommand(cmdShortBack);
            shortList.addCommand(cmdShortDial);
            shortList.addCommand(cmdShortAddCommand);
            shortList.addCommand(emptyShortCallListCommand);
            shortList.addCommand(shortNumbersEditCommand);
            shortList.addCommand(DELETECommand);
            shortList.setCommandListener(this);
            shortNums = new Vector(6);

            try {
                re = shortNumbers.enumerateRecords(null, null, false);
                while (re.hasNextElement()) {

                    byte[] b = re.nextRecord();

                    String pn = SimpleRecord.getPhoneNum(b);
                    shortList.append(SimpleRecord.getFirstName(b) + " " +
                                     SimpleRecord.getLastName(b) + " " +
                                     SimpleRecord.getPhoneNum(b), null);

                    System.out.println("Record: " + pn);
                    shortNums.addElement(pn);

                }
            } catch (Exception e) {
                displayAlert(ERROR, "Error while building name list: " + e,
                             null);
                return null;
            }
            Display.getDisplay(this).setCurrent(shortList);
        } else {
            displayAlert(INFO, "Kontaktlistan är tom!", getAddShortForm());
        }

        return shortList;
    }

    public void deleteAllShortRecords() throws RecordStoreException {

        RecordStore rs = null;
        RecordEnumeration re = null;
        try {
            rs = RecordStore.openRecordStore("ShortNumberList", true);
            re = rs.enumerateRecords(null, null, false);

            // First remove all records, a little clumsy.
            while (re.hasNextElement()) {
                int id = re.nextRecordId();
                rs.deleteRecord(id);
            }
        } catch (Exception ex) {
        }

    }

    private Screen genShortScr() {
        if (shortForm == null) {
            shortForm = new Form("Lägg till!");
            shortForm.addCommand(cmdShortCancel);
            shortForm.addCommand(cmdShortAdd);
            shortForm.setCommandListener(this);

            e_firstNameShort = new TextField("Förnamn:", "", FN_LEN,
                                             TextField.ANY);
            e_lastNameShort = new TextField("Efternamn:", "", LN_LEN,
                                            TextField.ANY);
            e_phoneNumShort = new TextField("Telefonnummer:", "", 5,
                                            TextField.PHONENUMBER);
            shortForm.append(e_firstNameShort);
            shortForm.append(e_lastNameShort);
            shortForm.append(e_phoneNumShort);
        }

        e_firstNameShort.delete(0, e_firstNameShort.size());
        e_lastNameShort.delete(0, e_lastNameShort.size());
        e_phoneNumShort.delete(0, e_phoneNumShort.size());

        Display.getDisplay(this).setCurrent(shortForm);

        return shortForm;
    }


    private void genDialShortScr() throws RecordStoreNotOpenException,
            InvalidRecordIDException, RecordStoreException {

        this.stringTotal = (String) shortNums.elementAt(shortList.
                getSelectedIndex());

        checkPhoneNumber();

        try {

            platformRequest("tel:" + switchBoardNumber + setP + accessCode +
                            stringTotal); // dial the number > DTMF-signals.

        } catch (ConnectionNotFoundException ex) {
        }
    }

    public void checkPhoneNumber() { // Justerar landsiffra som är inmatad! Tar bort '+' och lägger in '00' före landssiffran

        String Number = "+";
        String setNumber = "00";
        String validate = stringTotal;
        String validate46 = "46";
        String setNumberNoll = "0";

        if (Number.equals(validate.substring(0, 1)) &&
            validate46.equals(validate.substring(1, 3))) { // Om numret startar med '+' OCH '46' är sann så gör om till '0'

            accessCode = accessNumber;

            System.out.println("+46 är SANN gör om till 0 ");

            String setString = validate;

            String deletePartOfString = setString.substring(3); // ta bort plast 0 - 1 ur strängen....

            String setStringTotal = setNumberNoll + deletePartOfString; // sätt ihop strängen setStringTotal

            this.stringTotal = setStringTotal;

            System.out.println("Landsnummer är : " + stringTotal);

        }
        if (Number.equals(validate.substring(0, 1)) &&
            !validate46.equals(validate.substring(1, 3))) { // Om numret startar med '+' OCH 46 är falsk så gör om till '00'

            accessCode = accessNumber;

            System.out.println("Andra landsnummer tex +47 blir 00 SANN");

            String setString = validate;

            String deletePartOfString = setString.substring(1); // ta bort plast 0 - 1 ur strängen....

            String setStringTotal = setNumber + deletePartOfString; // sätt ihop strängen setStringTotal

            this.stringTotal = setStringTotal;

            System.out.println("Landsnummer: " + stringTotal);

        }
        if (!Number.equals(validate.substring(0, 1))) { // ring vanligt nummer

            accessCode = accessNumber;

            System.out.println("Telefonnummer: " + stringTotal);

        }
        if (validate.equals(validate.substring(0, 1)) ||
            validate.equals(validate.substring(0, 2)) ||
            validate.equals(validate.substring(0, 3)) ||
            validate.equals(validate.substring(0, 4)) ||
            validate.equals(validate.substring(0, 5))) {

            this.accessCode = "";

            System.out.println("Internnummer: " + stringTotal);

        }

    }

    private void addShortNumber() {
        String f = e_firstNameShort.getString();
        String l = e_lastNameShort.getString();
        String p = e_phoneNumShort.getString();

        byte[] b = SimpleRecord.createRecord(f, l, p);
        try {
            shortNumbers.addRecord(b, 0, b.length);
            displayAlert(INFO, "Anknytning tillagd",
                         genShortNumberScr("Ring anknytning", null, null, true));
        } catch (RecordStoreException rse) {
            displayAlert(ERROR, "Could not add record" + rse, mainList);
        }
    }

    // =========================================================================

    // Samtalslista MAX 25 stycken i databasen.

    public void saveDialedNumber(String dialedNumber) throws
            RecordStoreNotOpenException, RecordStoreException {

        String time = getTime();
        String emptyString = "";

        byte[] b = SimpleRecord.createRecord(time, emptyString, dialedNumber);
        addrBook.addRecord(b, 0, b.length);

        getDialednumberID();

    }

    public void getDialednumberID() throws RecordStoreNotOpenException,
            InvalidRecordIDException, RecordStoreException {

        sortRecords();

        RecordStore rs = null;

        try {
            rs = RecordStore.openRecordStore("TheAddressBook", true);
        } catch (RecordStoreException ex) {
        }

        String s1 = "", s2 = "";

        int ID = 0, recordID = 0;
        int countPostDB = 25; // Hur många poster i databasen som ska kunna adderas.

        Vector DialedIndex = new Vector();

        RecordEnumeration re = null;
        try {
            re = addrBook.enumerateRecords((RecordFilter)null,
                                           (RecordComparator)null, false);
        } catch (RecordStoreNotOpenException rsnoe) {
            System.err.println(rsnoe.toString());
        }

        while (re.hasNextElement()) { // går igenom alla element av index/ID i databasen.
            try {

                recordID = re.nextRecordId();

                System.out.println("TEST VILKET VÄRDE HAR recordID >> " +
                                   recordID);

                s1 = Integer.toString(recordID);

                s2 = new String(s1);

                DialedIndex.addElement(s2); // sparar alla index ur DB från Addrbook i vektorn DialedIndex. (osorterad).

            } catch (InvalidRecordIDException iride) {
                System.err.println(iride.toString());
            }
        }
        System.out.println("Skriver ut Vektorn DialedIndex innehåll >> " +
                           DialedIndex.toString());

        int objSize = DialedIndex.size(); // objSize sätter storleken på arrayen sortInteger

        int[] sortInteger = new int[objSize]; // Arrayen sortInteger skapas för att sortera värden ur vektorn DialedIndex.

        System.out.println("DialedIndex.size(); >> " + DialedIndex.size());

        for (int n = 0; n < DialedIndex.size(); ++n) {

            // sparar ner alla objekt ur vektorn till strängar.
            String stringToNewString = DialedIndex.elementAt(n).toString();

            // skapar en ny sträng av alla objekt....
            String stringToConvertString = new String(stringToNewString).
                                           toString();

            // Parsar ner strängarna till Integer(heltalsvärden) av typen int.
            int stringToInteger = Integer.parseInt(stringToConvertString);

            // sparar ner alla int-värden heltalsvärden i Arrayen.
            sortInteger[n] = stringToInteger;

        }
        // sorterar hela sortInteger-Arrayen.
        sortInteger(sortInteger);
        System.out.println();
        System.out.println("Sorterade alla heltalen i Arrayen sortInteger. ");
        for (int i = 0; i < sortInteger.length; i++) {
            System.out.print(sortInteger[i] + " ");

            ID = sortInteger[0];

        }
        System.out.println();

        System.out.println("Visar första index som ska motsvara ID >> " + ID);

        if (DialedIndex.size() >= countPostDB) {

            System.out.println("Skickar och DELETAR ID >> " + ID);
            deleteDialedNumbersRecord(ID);
            rs.closeRecordStore();
        }

        DialedIndex.removeAllElements();
        System.out.println("Visar vektorns innehåll efter remove >> " +
                           DialedIndex.toString());

    }

    public static void sortInteger(int[] v) {

        int t = 0;
        int counter = v.length - 1;
        for (int i = 0; i < v.length - 1; i++) {
            for (int j = 0; j < counter; j++) {
                if (v[j] > v[j + 1]) {
                    t = v[j];
                    v[j] = v[j + 1];
                    v[j + 1] = t;
                }
            }

            counter--;
        }
    }

    public void sortRecords() throws RecordStoreNotOpenException,
            InvalidRecordIDException, RecordStoreException {

        try {
            addrBook = RecordStore.openRecordStore("TheAddressBook", true);
        } catch (RecordStoreException e) {
            addrBook = null;
        }

        int lastID = addrBook.getNextRecordID();
        int numRecords = addrBook.getNumRecords();
        int count = 0;

        for (int id = 1;
                      id < lastID && count < numRecords;
                      ++id) {
            try {
                byte[] data = addrBook.getRecord(id);
                // process the data
                ++count;
            } catch (InvalidRecordIDException e) {
                // just ignore and move to the next record
            } catch (RecordStoreException e) {
                // a more general error that should be handled
                // somehow
                break;
            }
        }

    }

    public void deleteDialedNumbersRecord(int delete) throws
            RecordStoreNotOpenException,
            InvalidRecordIDException, RecordStoreException {

        /*String delete = deleteBox.getString();
                     int ID = 0;
                     ID = Integer.parseInt(delete);*/
        int p = 0;
        int recordID = delete;

        System.out.println("recordID >>>>>>>>>>>> " + recordID);

        //refreshShortnumbers();

        try {
            addrBook = RecordStore.openRecordStore("TheAddressBook", true);
        } catch (RecordStoreException e) {
            addrBook = null;
        }

        System.out.println("deleting ID #" + String.valueOf(recordID));
        /*
         * delete one of the string records
         */
        try {
            System.out.println("deleting ID #" + String.valueOf(recordID));
            addrBook.deleteRecord(recordID);
        } catch (RecordStoreException rse) {
            System.err.println(rse.toString());
        }

        //recordID = insertString("baz", recordStore);

        /*
         * Build an enumeration to index through all records. Notice
         * the hole where the one string record was deleted.
         */
        RecordEnumeration re = null;
        try {
            re = addrBook.enumerateRecords((RecordFilter)null,
                                           (RecordComparator)null, false);
        } catch (RecordStoreNotOpenException rsnoe) {
            System.err.println(rsnoe.toString());
        }

        while (re.hasNextElement()) {
            try {
                System.out.println("Next Record ID = " +
                                   re.nextRecordId());
            } catch (InvalidRecordIDException iride) {
                System.err.println(iride.toString());
            }
        }

        /*
         * Clean up the RecordEnumeration
         */


        /*
         * Build a new RecordEnumeration with a filter to include only
         * integer records. Notice how it will only report the integer
         * records.
         */
        IntegerFilter iFilt = new IntegerFilter();
        try {
            re = addrBook.enumerateRecords((RecordFilter) iFilt,
                                           null, false);
        } catch (RecordStoreNotOpenException rsnoe) {
            System.err.println(rsnoe.toString());
        }

        /*
         * And walk through it backwards for kicks...
         */
        while (re.hasPreviousElement()) {
            try {
                System.out.println("Previous Record ID = " +
                                   re.previousRecordId());
            } catch (InvalidRecordIDException iride) {
                System.err.println(iride.toString());
            }
        }

        /*
         * Clean up the RecordEnumeration
         */
        re.destroy();

        //Display.getDisplay(this).setCurrent(genNameScr(menyDefaultCalled, null, null, true));
        try {
            addrBook.closeRecordStore();
            //recordStore.deleteRecordStore(RECORD_STORE_NAME);
        } catch (RecordStoreException rse) {
            System.err.println(rse.toString());
        }

    }

    // GET ID, DELETE, SAVE AND UPDATE >>>> Ring anknytning.

    public void saveEditRecordsOfShortnumbers() throws InvalidRecordIDException,
            RecordStoreNotOpenException, RecordStoreException {

        int recordID = 0;
        String firstN = "", LastN = "", PhoneN = "";

        recordID = this.saveRecordID;

        RecordStore rs = null;
        try {
            rs = shortNumbers.openRecordStore("ShortNumberList", true);
        } catch (RecordStoreException ex) {
        }

        System.out.println("Skriver ut SPARAR recordID/Index >> " + recordID);

        //recordID = re.nextRecordId();

        firstN = editShortNumberTextFieldName.getString();
        LastN = editShortNumberTextFieldSurname.getString();
        PhoneN = editShortNumberTextFieldExtension.getString();

        byte[] b = SimpleRecord.createRecord(firstN, LastN, PhoneN);

        try {
            rs.setRecord(recordID, b, 0, b.length);
        } catch (Exception e) {

        }
        genShortNumberScr("Ring anknytning", null, null, true);

        rs.closeRecordStore();

    }

    private void checkSaveEditInPutShortNumber() {

        editShortNumberTextFieldName.getString();
        editShortNumberTextFieldSurname.getString();
        editShortNumberTextFieldExtension.getString();

        if (editShortNumberTextFieldName.getString().equals("") ||
            editShortNumberTextFieldExtension.getString().equals("")) {

            displayAlert(INPUT, "Fel inmatning!\n Försök igen!",
                         genShortNumberScr("Ring anknytning", null, null, true));

            // ALERT..............

        } else {

            try {
                saveEditRecordsOfShortnumbers();
            } catch (RecordStoreNotOpenException ex) {
            } catch (InvalidRecordIDException ex) {
            } catch (RecordStoreException ex) {
            }
        }

    }


    public void editRecordsOfShortnumbers() throws RecordStoreNotOpenException,
            InvalidRecordIDException, RecordStoreException { // hämtar valt index-värde, record och kan editeras.

        int recordID = 0;

        RecordStore rs = null;
        try {
            rs = shortNumbers.openRecordStore("ShortNumberList", true);
        } catch (RecordStoreException ex) {
        }

        recordID = getID(recordID);
        this.saveRecordID = recordID;

        System.out.println("Skriver ut recordID/Index >> " + recordID);

        //recordID = re.nextRecordId();

        byte[] b = rs.getRecord(recordID);

        String firstN = SimpleRecord.getFirstName(b);
        String LastN = SimpleRecord.getLastName(b);
        String PhoneN = SimpleRecord.getPhoneNum(b);

        System.out.println("Skriver ut hela EDIT strängen >> " + firstN + " " +
                           LastN + " " + PhoneN);

        editShortNumberTextFieldName.setString(firstN);
        editShortNumberTextFieldSurname.setString(LastN);
        editShortNumberTextFieldExtension.setString(PhoneN);

        Display.getDisplay(this).setCurrent(getEditShortNumbersForm());

        rs.closeRecordStore();

    }


    public int checkShortnumberSize() throws RecordStoreNotOpenException {
        // Metoden kontrollerar att databasen max innehåller 25 stycken anknytningar.

        Vector vCheckSize = new Vector();
        String str = "";
        int size = 0;
        RecordEnumeration re = null;

        try {
            re = shortNumbers.enumerateRecords((RecordFilter)null,
                                               (RecordComparator)null, false);
        } catch (RecordStoreNotOpenException rsnoe) {
            System.err.println(rsnoe.toString());
        }
        if (shortNumbers.getNumRecords() > 0) {
            re = shortNumbers.enumerateRecords(null, null, false);
            while (re.hasNextElement()) {

                try {
                    str = new String(re.nextRecord());
                    vCheckSize.addElement(str);
                } catch (RecordStoreNotOpenException ex) {
                } catch (InvalidRecordIDException ex) {
                } catch (RecordStoreException ex) {
                }
            }
            for (int n = 0; n < vCheckSize.size(); ++n) {

                size = vCheckSize.size();

                System.out.println("SIZEEEEEEEEE  >> " + size);

            }
        }

        return size;
    }


    private void checkInPutShortNumber() throws RecordStoreNotOpenException {

        String F_String = e_firstNameShort.getString();
        String L_String = e_lastNameShort.getString();
        String P_String = e_phoneNumShort.getString();

        int setSize = checkShortnumberSize();
        int maxSize = 25;

        if (e_firstNameShort.getString().equals("") ||
            e_phoneNumShort.getString().equals("")) {

            displayAlert(INFO, "Kunde inte lägga till anknyting",
                         genShortNumberScr("Ring anknytning", null, null, true));

        } else if (setSize >= maxSize) { // Databasen får max innehålla värdet av maxSize.

            displayAlert(SIZE, "Maximalt 25 stycken anknytningar är tillagda!",
                         genShortNumberScr("Ring anknytning", null, null, true));

        } else {

            addShortNumbers(F_String, L_String, P_String);
        }

    }

    public void addShortNumbers(String f, String l, String p) {

        writeShortNumbers();
        refreshShortnumbers();

        byte[] b = SimpleRecord.createRecord(f, l, p);

        try {
            shortNumbers.addRecord(b, 0, b.length);
            displayAlert(INFO, "Anknytning tillagd!",
                         genShortNumberScr("Ring anknytning", null, null, true));
        } catch (RecordStoreException rse) {
            displayAlert(ERROR, "Kunde inte lägga till anknyting" + rse,
                         genShortNumberScr("Ring anknytning", null, null, true));
        }

        RecordEnumeration re = null;
        try {
            re = shortNumbers.enumerateRecords((RecordFilter)null,
                                               (RecordComparator)null, false);
        } catch (RecordStoreNotOpenException rsnoe) {
            System.err.println(rsnoe.toString());
        }

        while (re.hasNextElement()) {
            try {
                System.out.println("Next Record ID = " +
                                   re.nextRecordId());
                /*Vector v = new Vector(6);
                              v.addElement(new Integer(re.nextRecordId()+1));
                              String s = v.toString();
                 System.out.println("Skriver ut innehållet ur vektorn v >>> " + s);*/

            } catch (InvalidRecordIDException iride) {
                System.err.println(iride.toString());
            }
        }

        /*
         * Clean up the RecordEnumeration
         */
        re.destroy();

    }

    public void writeShortNumbers() {

        try {
            System.out.println("Antal poster i databasen shortNumbers: " +
                               shortNumbers.getNumRecords());

            if (shortNumbers.getNumRecords() > 0) {
                RecordEnumeration re = shortNumbers.enumerateRecords(null, null, false);
                while (re.hasNextElement()) {
                    String str = new String(re.nextRecord());
                    System.out.println(
                            "Skriver ut poster ur databasen shortNumbers: " +
                            str);

                    updateDBShortNumbers();
                }
            }

        } catch (Exception e) {
            System.err.println(e.toString());
        }

    }

    public void updateDBShortNumbers() {
        try {
            System.out.println("Antal poster i databasen shortNumbers: " +
                               shortNumbers.getNextRecordID());

            if (shortNumbers.getNumRecords() > 0) {
                RecordEnumeration re = shortNumbers.enumerateRecords(null, null, false);
                while (re.hasNextElement()) {
                    String str = new String(re.nextRecord());
                    str.length();
                    System.out.println("Databasen shortNumbers uppdateras: " +
                                       str.trim());
                }
            }

        } catch (Exception e) {
            System.err.println(e.toString());
        }
    }

    public void refreshShortnumbers() {

        RecordStore rs = null;
        try {
            rs = shortNumbers.openRecordStore("ShortNumberList", true);
        } catch (RecordStoreException ex) {
        }

        byte[] recData = new byte[5];
        int len;

        try {
            for (int i = 1; i <= rs.getNumRecords(); i++) {
                // Allocate more storage if necessary
                try {
                    if (rs.getRecordSize(i) > recData.length) {
                        recData = new byte[rs.getRecordSize(i)];
                    }
                } catch (InvalidRecordIDException ex1) {
                } catch (RecordStoreNotOpenException ex1) {
                } catch (RecordStoreException ex1) {
                }

                len = rs.getRecord(i, recData, 0);
            }
        } catch (InvalidRecordIDException ex2) {
        } catch (RecordStoreNotOpenException ex2) {
        } catch (RecordStoreException ex2) {
        }
    }

    public void deleteShortNumbersRecord() throws RecordStoreNotOpenException,
            InvalidRecordIDException, RecordStoreException {

        int p = 0;
        int recordID = getID(p);

        System.out.println("recordID >>>>>>>>>>>> " + recordID);

        //refreshShortnumbers();

        try {
            shortNumbers = RecordStore.openRecordStore("ShortNumberList", true);
        } catch (RecordStoreException e) {
            shortNumbers = null;
        }

        System.out.println("deleting ID #" + String.valueOf(recordID));
        /*
         * delete one of the string records
         */
        try {
            System.out.println("deleting ID #" + String.valueOf(recordID));
            shortNumbers.deleteRecord(recordID);
        } catch (RecordStoreException rse) {
            System.err.println(rse.toString());
        }

        //recordID = insertString("baz", recordStore);

        /*
         * Build an enumeration to index through all records. Notice
         * the hole where the one string record was deleted.
         */
        RecordEnumeration re = null;
        try {
            re = shortNumbers.enumerateRecords((RecordFilter)null,
                                               (RecordComparator)null, false);
        } catch (RecordStoreNotOpenException rsnoe) {
            System.err.println(rsnoe.toString());
        }

        while (re.hasNextElement()) {
            try {
                System.out.println("Next Record ID = " +
                                   re.nextRecordId());
            } catch (InvalidRecordIDException iride) {
                System.err.println(iride.toString());
            }
        }

        /*
         * Clean up the RecordEnumeration
         */


        /*
         * Build a new RecordEnumeration with a filter to include only
         * integer records. Notice how it will only report the integer
         * records.
         */
        IntegerFilter iFilt = new IntegerFilter();
        try {
            re = shortNumbers.enumerateRecords((RecordFilter) iFilt,
                                               null, false);
        } catch (RecordStoreNotOpenException rsnoe) {
            System.err.println(rsnoe.toString());
        }

        /*
         * And walk through it backwards for kicks...
         */
        while (re.hasPreviousElement()) {
            try {
                System.out.println("Previous Record ID = " +
                                   re.previousRecordId());
            } catch (InvalidRecordIDException iride) {
                System.err.println(iride.toString());
            }
        }

        /*
         * Clean up the RecordEnumeration
         */
        re.destroy();

        try {
            shortNumbers.closeRecordStore();
            //recordStore.deleteRecordStore(RECORD_STORE_NAME);
        } catch (RecordStoreException rse) {
            System.err.println(rse.toString());
        }
    }

    public int getID(int getRecordIndex) throws RecordStoreNotOpenException {

        String s1 = "", s2 = "", s3 = "", s4 = "", s5 = "", s6 = "", s7 = "",
                str = "";

        int ID = 0, recordID = 0;

        Vector index = new Vector();
        Vector vShort = new Vector();
        Vector getIndex = new Vector();

        String selectIndex = (String) shortNums.elementAt(shortList.
                getSelectedIndex());

        RecordEnumeration re = null;
        try {
            re = shortNumbers.enumerateRecords((RecordFilter)null,
                                               (RecordComparator)null, false);
        } catch (RecordStoreNotOpenException rsnoe) {
            System.err.println(rsnoe.toString());
        } while (re.hasNextElement()) { // går igenom alla element av index/ID i databasen.
            try {

                recordID = re.nextRecordId();

                s1 = Integer.toString(recordID);

                s5 = new String(s1);

                index.addElement(s5);

            } catch (InvalidRecordIDException iride) {
                System.err.println(iride.toString());
            }
        }

        if (shortNumbers.getNumRecords() > 0) {
            re = shortNumbers.enumerateRecords(null, null, false);
            while (re.hasNextElement()) {

                try {
                    str = new String(re.nextRecord());
                    vShort.addElement(str);
                } catch (RecordStoreNotOpenException ex) {
                } catch (InvalidRecordIDException ex) {
                } catch (RecordStoreException ex) {
                }
            }
            for (int j = 0; j < index.size(); ++j) {

                s6 = index.elementAt(j).toString();

                for (int p = 0; p < vShort.size(); ++p) {

                    s7 = vShort.elementAt(j).toString();

                }

                s2 = s6 + s7;

                getIndex.addElement(s2);

            }
            for (int n = 0; n < getIndex.size(); ++n) {

                String s8 = getIndex.elementAt(n).toString();

                s4 = s8.substring(0, 2); // 1 - 2 siffrigt id-index tas ut ur strängen s

                s4 = sortCharAt(s4); // rensar blanksteg och tecken >> blir endast siffror kvar.

                s3 = s8.substring(3, s8.length()); // tar ut upp till 5 siffrig anknytning.

                s3 = sortCharAt(s3); // rensar blanksteg och tecken >> blir endast siffror kvar.

                if (s3.equals(selectIndex)) {

                    String IDs = s4;

                    ID = Integer.parseInt(IDs);
                }

            }

        }
        return ID;
    }

    public String sortCharAt(String s) {

        String sString = s; // sortString innehåller samma som för IMEI-strängen för att kunna kontrollera å sortera bort tecken....

        StringBuffer bTecken = new StringBuffer(sString); // Lägg strängen sortString i ett stringbuffer objekt...

        for (int i = 0; i < bTecken.length(); i++) { // räkna upp hela bTecken-strängens innehåll hela dess längd

            char tecken = bTecken.charAt(i); // char tecken är innehållet i hela längden

            // Tecknen täcker >> Tyska, Svenska, Engelska, Norska, Danska.

            if ('A' <= tecken && tecken <= 'Z' ||
                'a' <= tecken && tecken <= 'z'
                || tecken == '-' || tecken == '/' || tecken == '\\' ||
                tecken == ':' || tecken == ';'
                || tecken == '.' || tecken == ',' || tecken == '_' ||
                tecken == '|' || tecken == '<'
                || tecken == '>' || tecken == '+' || tecken == '(' ||
                tecken == ')' || tecken == 'å' || tecken == 'Å' ||
                tecken == 'ä' || tecken == 'Ä' || tecken == 'ö' ||
                tecken == 'Ö' ||
                tecken == 'Ü' || tecken == 'ü' || tecken == 'ß' ||
                tecken == 'Æ' || tecken == 'æ' || tecken == 'Ø' ||
                tecken == 'ø') {

                bTecken.setCharAt(i, ' '); // lägg in blanksteg i strängen där något av ovanstående tecken finns....
            }

        }

        bTecken.append(' '); // lägger till blanksteg sist i raden så att sista kommer med för att do-satsen ska kunna hitta och sortera...

        String setString = new String(bTecken); // Gör om char-strängen till en string-sträng

        int antal = 0;
        char separator = ' '; // för att kunna sortera i do-satsen

        int index = 0;

        do { // do satsen sorterar ut blankstegen och gör en ny sträng för att jämföra IMEI med...
            ++antal;
            ++index;

            index = setString.indexOf(separator, index);
        } while (index != -1);

        subStr = new String[antal];
        index = 0;
        int slutindex = 0;

        for (int j = 0; j < antal; j++) {

            slutindex = setString.indexOf(separator, index);

            if (slutindex == -1) {
                subStr[j] = setString.substring(index);
            }

            else {
                subStr[j] = setString.substring(index, slutindex);
            }

            index = slutindex + 1;

        }
        String setNumber = "";
        for (int k = 0; k < subStr.length; k++) {

            setNumber += subStr[k]; // Lägg in värdena från subStr[k] i strängen setNumber....
        }

        System.out.println("Sorterad: " + setNumber);

        String sortedString = setNumber;

        return sortedString;
    }


}


//--------------NEW CLASS ------------------------------------------------------



class SimpleComparator implements RecordComparator {

    /**
     * Sorting values (sort by first or last name)
     */
    public final static int SORT_BY_FIRST_NAME = 1;

    public final static int SORT_BY_LAST_NAME = 2;

    /**
     * Sort order. Set by constructor.
     */
    private int sortOrder = -1;

    /**
     * Public constructor: sets the sort order to be used for this
     * instantiation. Sanitize s: if it is not one of the valid sort codes, set
     * it to SORT_BY_LAST_NAME silently. s the desired sort order
     *
     * @param s int
     */
    SimpleComparator(int s) {
        switch (s) {
        case SORT_BY_FIRST_NAME:
        case SORT_BY_LAST_NAME:
            this.sortOrder = s;
            break;
        default:
            this.sortOrder = SORT_BY_LAST_NAME;
            break;
        }
    }

    /**
     * This is the compare method. It takes two records, and depending on the
     * sort order extracts and lexicographically compares the subfields as two
     * Strings. r1 First record to compare r2 Second record to compare return
     * one of the following: RecordComparator.PRECEDES if r1 is
     * lexicographically less than r2 RecordComparator.FOLLOWS if r1 is
     * lexicographically greater than r2 RecordComparator.EQUIVALENT if r1 and
     * r2 are lexicographically equivalent
     *
     * @param r1 byte[]
     * @param r2 byte[]
     * @return int
     */
    public int compare(byte[] r1, byte[] r2) {

        String n1 = null;
        String n2 = null;

        // Based on sortOrder, extract the correct fields
        // from the record and convert them to lower case
        // so that we can perform a case-insensitive compare.
        if (sortOrder == SORT_BY_FIRST_NAME) {
            n1 = SimpleRecord.getFirstName(r1).toLowerCase();
            n2 = SimpleRecord.getFirstName(r2).toLowerCase();
        } else if (sortOrder == SORT_BY_LAST_NAME) {
            n1 = SimpleRecord.getLastName(r1).toLowerCase();
            n2 = SimpleRecord.getLastName(r2).toLowerCase();
        }

        int n = n1.compareTo(n2);
        if (n < 0) {
            return RecordComparator.PRECEDES;
        }
        if (n > 0) {
            return RecordComparator.FOLLOWS;
        }

        return RecordComparator.EQUIVALENT;
    }
}


final class SimpleRecord {

    private final static int FIRST_NAME_INDEX = 0;

    private final static int LAST_NAME_INDEX = 20;

    private final static int FIELD_LEN = 20;

    private final static int PHONE_INDEX = 40;

    private final static int MAX_REC_LEN = 60;

    private static StringBuffer recBuf = new StringBuffer(MAX_REC_LEN);

    // Don't let anyone instantiate this class
    private SimpleRecord() {
    }

    // Clear internal buffer
    private static void clearBuf() {
        for (int i = 0; i < MAX_REC_LEN; i++) {
            recBuf.insert(i, ' ');
        }
        recBuf.setLength(MAX_REC_LEN);
    }

    /**
     * Takes component parts and return a record suitable for our address book.
     * return byte[] the newly created record first record field: first name
     * last record field: last name num record field: phone number
     *
     * @param first String
     * @param last String
     * @param num String
     * @return byte[]
     */
    public static byte[] createRecord(String first, String last, String num) {
        clearBuf();
        recBuf.insert(FIRST_NAME_INDEX, first);
        recBuf.insert(LAST_NAME_INDEX, last);
        recBuf.insert(PHONE_INDEX, num);
        recBuf.setLength(MAX_REC_LEN);
        return recBuf.toString().getBytes();
    }

    /**
     * Extracts the first name field from a record. return String contains the
     * first name field b the record to parse
     *
     * @param b byte[]
     * @return String
     */
    public static String getFirstName(byte[] b) {
        return new String(b, FIRST_NAME_INDEX, FIELD_LEN).trim();
    }

    /**
     * Extracts the last name field from a record. return String contains the
     * last name field b the record to parse
     *
     * @param b byte[]
     * @return String
     */
    public static String getLastName(byte[] b) {
        return new String(b, LAST_NAME_INDEX, FIELD_LEN).trim();
    }

    /**
     * Extracts the phone number field from a record. return String contains the
     * phone number field b the record to parse
     *
     * @param b byte[]
     * @return String
     */
    public static String getPhoneNum(byte[] b) {
        return new String(b, PHONE_INDEX, FIELD_LEN).trim();
    }
}


class SimpleFilter implements RecordFilter {

    // first and last names on which to filter
    private String first;

    private String last;

    /**
     * Public constructor: stores the first and last names on which to filter.
     * Stores first/last names as lower case so that filters are are
     * case-insensitive.
     *
     * @param f String
     * @param l String
     */
    public SimpleFilter(String f, String l) {
        first = f.toLowerCase();
        last = l.toLowerCase();
    }

    /**
     * Takes a record, (r), and checks to see if it matches the first and last
     * name set in our constructor. Extracts the first and last names from the
     * record, converts them to lower case, then compares them with the values
     * extracted from the record. return true if record matches, false otherwise
     *
     * @param r byte[]
     * @return boolean
     */
    public boolean matches(byte[] r) {

        String f = SimpleRecord.getFirstName(r).toLowerCase();
        String l = SimpleRecord.getLastName(r).toLowerCase();

        return f.startsWith(first) && l.startsWith(last);
    }
}
