import javax.management.Query;
import java.rmi.ConnectIOException;
import java.sql.*;

public class DatabaseCon {

    final String dbName = "gallery";
    final String username = "root";
    final String password = "admin";
    final String insertQuery = "INSERT INTO `gallery`.`galleryobjs` (`objName`, `objLnkCreated`, `objThumbnail`, `objTags`) VALUES (?, ?, ?, ?)";
    String fillTable;
    String fp;

    Connection con;

    //constructor
    public DatabaseCon(String fp){

        //INSERT INTO `gallery`.`galleryobjs` (`objName`, `objLnkCreated`, `objThumbnail`, `objTags`) VALUES (?, ?, ?, ?);
        // ^^ query to add a row in database table
        //System.out.println(sqlCon()!=null);
        //addEntry("[ABBB] Star Guardian Comic [English]", true, "M:/tempTest/ABBB/[ABBB] Star Guardian Comic [English]/1.jpg", "ABBB");
        this.fp = fp;

    };

    public boolean addEntry(String imgSetName, Boolean linkState, String thumbnailFp, String imgSetTags){

        try {

            this.con = sqlCon();
            PreparedStatement preparedStatement = con.prepareStatement(insertQuery);
            preparedStatement.setString(1, imgSetName);
            preparedStatement.setBoolean(2, linkState);
            preparedStatement.setString(3, thumbnailFp);
            preparedStatement.setString(4, imgSetTags);

            preparedStatement.executeUpdate();

            System.out.println("Added entry successfully");
            this.con.close();

        } catch (SQLIntegrityConstraintViolationException e){

            System.out.println("Entry is already in db");
            try{
                this.con.close();
            }catch(SQLException exception){
                exception.printStackTrace();
            }

            return false;
            //e.printStackTrace();

        }catch (Exception e){

            System.out.println("could not add entry : ");
            e.printStackTrace();

        }

        return true;

    }

    public void createNewEntryTable(String[] imgSet, String imgsetName){

        try {

            this.con = sqlCon();
            Statement statement = this.con.createStatement();
            String getId = "select `idGalleryObjs` from gallery.galleryobjs where objName = '"+ imgsetName +"';";
            ResultSet idRs = statement.executeQuery(getId);
            idRs.next();
            String imgSetID = String.valueOf(idRs.getInt(1));

            String createTable = "CREATE TABLE `gallery`.`"+ imgSetID +"` (\n" +
                    "  `pageNum` INT NOT NULL AUTO_INCREMENT,\n" +
                    "  `pageLink` VARCHAR(250) NOT NULL,\n" +
                    "  PRIMARY KEY (`pageNum`),\n" +
                    "  UNIQUE INDEX `idimgsetname_UNIQUE` (`pageNum` ASC) VISIBLE,\n" +
                    "  UNIQUE INDEX `pageLink_UNIQUE` (`pageLink` ASC) VISIBLE);\n";
            statement.execute(createTable);


            for(int i=0; i<imgSet.length; i++) {
                this.fillTable = "INSERT INTO `gallery`.`"+ imgSetID +"` (`pageLink`) VALUES (?)";

                PreparedStatement preparedStatement = con.prepareStatement(this.fillTable);
                preparedStatement.setString(1, this.fp + "/" + imgSet[i]);

                preparedStatement.executeUpdate();

            }

            this.con.close();

        } catch (SQLException e){

            try{
                this.con.close();
            }catch(SQLException exception){
                exception.printStackTrace();
            }
            e.printStackTrace();

        }

    }

    // add thumbnail path to pass through, change return to int
    public boolean alreadyInDb(String name){
        int existsInt=0;

        try{

            this.con = sqlCon();
            Statement statement = this.con.createStatement();

            // select thumbnail path as well
            ResultSet exists = statement.executeQuery("select idGalleryObjs from gallery.galleryobjs where objName = '"+ name +"';");

            // check to see if thumbnail paths match , return 2 if yes (means the same img set from same path) 1 means path needs to be updated
            // 0 means entry doesnt exist.
            exists.next();
            existsInt = exists.getInt(1);

        }catch(SQLException e){

            //e.printStackTrace();
            System.out.println("not in db");
            return false;

        }

        System.out.println(existsInt);
        if(existsInt != 0){ return true; }
        else { return false; }

    }

    public void printTable(){

        try {

            this.con = sqlCon();
            Statement statement = this.con.createStatement();
            ResultSet resultSet = statement.executeQuery("select * from galleryobjs");

            while(resultSet.next()){

                System.out.println(resultSet.getInt(1) + " || " + resultSet.getString(2) + " || " + resultSet.getBoolean(3) + " || " + resultSet.getString(4) + " || " + resultSet.getString(5));

            }

        } catch (SQLException e){

            e.printStackTrace();

        }
    }

/*
    private boolean duplicateCheck(String str){



    }

 */

    //Connect to Mysql database
    private Connection sqlCon(){

        try {

            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection("jdbc:mysql://localhost:3306/" + getDbName(), getUsername(), getPassword());

        } catch (Exception e){

            e.printStackTrace();
            System.out.println("null returned");

        }

        return null;

    }

    private String getDbName(){

        return dbName;

    }

    private String getUsername(){

        return username;

    }

    private String getPassword(){

        return password;

    }

}
