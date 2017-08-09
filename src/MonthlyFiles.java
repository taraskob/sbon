class MonthlyFiles {

    static void CreateFiles(String day_begin, String day_end, String day_on) {
        WriteFileThread wft = new WriteFileThread(day_begin, day_end, day_on);
        try {
            CreateFile filenbu = new File_05();
            wft.WritToFile(filenbu);
            filenbu = new File_06();
            wft.WritToFile(filenbu);
            filenbu = new File_77();
            wft.WritToFile(filenbu);
            filenbu = new File_91();
            wft.WritToFile(filenbu);
            filenbu = new File_A7();
            wft.WritToFile(filenbu);
            wft.t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
