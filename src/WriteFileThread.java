class WriteFileThread implements Runnable {
    CreateFile FileObj;
    String day_begin, day_end, day_on;

    WriteFileThread(String day1, String day2, String day3) {
        this.day_begin = day1;
        this.day_end = day2;
        this.day_on = day3;
    }

    Thread t;

    @Override
    public void run() {
        FileObj.CreateFileNN(day_begin, day_end, day_on);
    }

    public void WritToFile(CreateFile filenbu) {
        this.FileObj = filenbu;
        t = new Thread(this);
        t.start();
    }
}
