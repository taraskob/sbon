class FileCtrl implements FileCreateListener {
    private FileCtrl() {
    }

    private static class ControllerHandler {
        private static FileCtrl CTRL;

        static {

            CTRL = new FileCtrl();
        }
    }

    public static FileCtrl getController() {

        return ControllerHandler.CTRL;
    }

    @Override
    public void fireListener(String message) {
        System.out.println(message);
    }
}
