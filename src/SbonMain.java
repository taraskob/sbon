class SbonMain {

    public static void main(String[] args) {
        String DBname = "sbon";
        String uploadDIR = "C:/ProgramData/MySQL/MySQL Server 5.7/Uploads/";
        String DBSTRUCTFILE = "DB_Structure.csv";
     /*
     try {
            SbonCreateSchemaDB.CreateSchemaDB();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        */
        // SbonInsertData.InsertData();
        SbonCreateSchemaDBFromFile.CreateSchemaDBFromFile(DBname, uploadDIR);
    }
}
