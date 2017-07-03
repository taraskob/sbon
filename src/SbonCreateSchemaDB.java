import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

class SbonCreateSchemaDB {
    static void CreateSchemaDB() throws SQLException {
        Connection Conn = SbonDBConnection.CreateDBConnection();
        if (Conn == null)
            return;
        Statement s = Conn.createStatement();
        int Result = s.executeUpdate("CREATE SCHEMA IF NOT EXISTS sbon CHARACTER SET cp1251");
        s.executeUpdate("USE sbon");
        s.executeUpdate("CREATE TABLE IF NOT EXISTS sbon.ACCOUNT (" +
                "  AsNumber INT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'Реєстраційний номер рахунку або код контрагента'," +
                "  AccountNumber CHAR(10) NOT NULL COMMENT 'Повний номер рахунку з контрольними розрядами '," +
                "  Name VARCHAR(75) NOT NULL COMMENT 'Прізвище клієнта'," +
                "  StatusAccount CHAR(18) NOT NULL COMMENT 'Статус рахунку'," +
                "  CardSeqNumber VARCHAR(4) NULL COMMENT 'Порядковий номер фінансової карточки з ознакою блокування'," +
                "  DateEndFinCard DATE NOT NULL COMMENT 'Дата закінчення терміну дії фінансової карти'," +
                "  PN_Vyp INT NULL COMMENT 'Порядковий номер поточної виписки (відлік номерів виписок у межах поточного року починається з 1 і послідовно змінюється по мірі видачі виписки клієнту)'," +
                "  PN_opVyp INT NULL COMMENT 'Наступний порядковий номер операції у виписці'," +
                "  Deposit_Num INT NULL COMMENT 'К-сть депозитів на рахунку ( нумерація починається з 1)'," +
                "  PRIMARY KEY (AsNumber)," +
                "  UNIQUE INDEX AsNumber_UNIQUE (AsNumber ASC)," +
                "  UNIQUE INDEX AccountNumber_UNIQUE (AccountNumber ASC)," +
                "  INDEX Name (Name ASC))" +
                "ENGINE = InnoDB DEFAULT CHARSET=utf8mb4;");
        s.executeUpdate("CREATE TABLE IF NOT EXISTS sbon.VKLADY (" +
                "  AsNumber INT UNSIGNED NOT NULL COMMENT 'Реєстраційний номер рахунку або код контрагента'," +
                "  AccountNumber CHAR(10) NOT NULL COMMENT 'Повний номер рахунку з контрольними розрядами '," +
                "  Name VARCHAR(75) NOT NULL COMMENT 'Прізвище клієнта'," +
                "  VklNum INT NOT NULL COMMENT 'Порядковий номер вкладу - договору на вклад\'," +
                "  ShifrVkl INT NOT NULL COMMENT 'Шифр вкладу  з  довідника вкладів \'," +
                "  VklTyp INT NOT NULL COMMENT 'Тип  вкладу: 0 – поточний ( основний); 1 – строковий; 2 - чековий ...'," +
                "  StatusVkl INT NULL COMMENT 'Ознака ( 0 – відкритий , 1 – блокований )'," +
                "  ProcSt VARCHAR (10)  NULL COMMENT 'Процентна ставка'," +
                "  Data_Vidk DATE NOT NULL COMMENT 'Дата відкриття вкладу'," +
                "  Data_Zakr DATE NULL COMMENT 'Дата закриття вкладу'," +
                "  Suma INT NOT NULL COMMENT 'Сума вкладу'," +
                "  KodVal INT NULL COMMENT 'Код валюти'," +
                "  DataOp DATE NULL COMMENT 'Дата останнього умовного нарахування процентів або останньої операції'," +
                "  ProcUNar INT NULL COMMENT '% умовно нарахований за період від відкриття депозиту до поточної дати.\'," +
                "  Oz7041_1 CHAR(14) NOT NULL COMMENT 'Операційні затрати  НЕ виплачені і НЕ оподатковані'," +
                "  Oz7041_2 CHAR(14) NOT NULL COMMENT 'Операційні затрати  НЕ виплачені і Оподатковані'," +
                "  Oz7041_3 CHAR(14) NULL COMMENT 'Операційні затрати  Виплачені і НЕ оподатковані.'," +
                "  Oz7041_4 CHAR(14) NULL COMMENT 'Операційні затрати  Виплачені і Оподатковані'," +
                "  ProcNar INT NULL COMMENT '% умовно нарахований за період від відкриття депозиту до поточної дати.'," +
                "  Data_Nar DATE NULL COMMENT 'Дата останнього нарахування процентів'," +
                "  Proc_Zar INT NULL COMMENT 'Зарахований (виплачений) процент  - (2620)'," +
                "  Data_Zar DATE NULL COMMENT 'Дата останнього зарахування процентів (Капіталізації)\'," +
                "  Suma_P INT NOT NULL COMMENT 'Сума вкладу початкова'," +
                "  Proc_DoZakr INT NULL COMMENT 'Умовно нарахований %  від  відкриття до останнього нарахування % '," +
                "  Proc_Min INT NULL COMMENT '%  Знятий з зарахованого або перерахований на вклад ДЗП'," +
                "  Proccount INT NULL COMMENT 'Процентне число'," +
                "  INDEX AsNumber_idx (AsNumber ASC)," +
                "  INDEX vklady (AsNumber ASC, Data_Vidk ASC, KodVal ASC, VklNum ASC)," +
                "  CONSTRAINT AsNumber" +
                "    FOREIGN KEY (AsNumber)" +
                "    REFERENCES sbon.ACCOUNT (AsNumber)" +
                "    ON DELETE RESTRICT" +
                "    ON UPDATE CASCADE)" +
                "ENGINE = InnoDB;");
        s.executeUpdate("CREATE TABLE IF NOT EXISTS sbon.ACNTBALN (" +
                "  asNumber INT UNSIGNED NOT NULL COMMENT 'Реєстраційний номер рахунку'," +
                "  AnAccount CHAR(14) NULL COMMENT 'Аналітичний номер  рахунку'," +
                "  OznAP CHAR(2) NULL COMMENT 'Ознака рахунку :   А - акстивний , П- пасивний ,АП -  активно-пасивний'," +
                "  Nazva VARCHAR(50) NULL COMMENT 'Назва  аналітичного  рухунку\'," +
                "  StatusAccount INT NULL COMMENT 'Статус рахунку'," +
                "  KodVal INT NULL COMMENT 'Код   валюти'," +
                "  KodStrok INT NULL COMMENT 'Код строку зберігання'," +
                "  Suma INT NULL COMMENT 'Поточний залишок коштів на рахунку'," +
                "  SaldoR INT NULL COMMENT 'Сальдо  на  початок року'," +
                "  ObDd INT NULL COMMENT 'Обороти дебетові за день \'," +
                "  ObKd INT NULL COMMENT 'Обороти кредитові за день'," +
                "  ObDm INT NULL COMMENT 'Обороти  дебетові  з  поч.  Місяця'," +
                "  ObKm INT NULL COMMENT 'Обороти  кредитові  з поч. місяця'," +
                "  ObDk INT NULL COMMENT 'Обороти  дебетові  з поч. кварталу '," +
                "  ObKk INT NULL COMMENT 'Обороти  кредитові  з поч. Кварталу'," +
                "  ObDr INT NULL COMMENT 'Обороти  дебетові  з поч.  Року'," +
                "  ObKr INT NULL COMMENT 'Обороти  кредитові  з поч.  Року'," +
                "  PRIMARY KEY (asNumber)," +
                "  UNIQUE INDEX asNumber_UNIQUE (asNumber ASC)," +
                "  INDEX AnAccount_UNIQUE (AnAccount ASC))" +
                "ENGINE = InnoDB;");
        s.executeUpdate("CREATE TABLE IF NOT EXISTS sbon.CLIENT (" +
                "  AsNumber INT UNSIGNED NOT NULL COMMENT 'Реєстраційний номер рахунку'," +
                "  PAN CHAR(19) NOT NULL COMMENT 'ПАНкод'," +
                "  Status INT NULL COMMENT 'Статус рахунку'," +
                "  DataOpen DATE NOT NULL COMMENT 'Дата відкриття'," +
                "  DateChange DATE NULL COMMENT 'Дата зміни даних контрагента.'," +
                "  DataClose DATE NULL COMMENT 'Дата закриття'," +
                "  IDN_Podatk CHAR(10) NULL COMMENT 'Ідентифікаційний податковий код'," +
                "  Name VARCHAR(75) NOT NULL COMMENT 'Прізвище клієнта'," +
                "  Address VARCHAR(100) NULL COMMENT 'Адреса клієнта'," +
                "  Phone VARCHAR(18) NULL COMMENT 'Телефон'," +
                "  BirthDate DATE NULL COMMENT 'Дата  народження'," +
                "  AddressBD VARCHAR(100) NULL COMMENT 'Місце  народження'," +
                "  Passport CHAR(18) NULL COMMENT 'Серія і номер паспорта'," +
                "  PassportDate DATE NULL COMMENT 'Дата видачі паспорта'," +
                "  PassportWhere VARCHAR(70) NULL COMMENT 'Ким і коли виданий'," +
                "  Resident INT NULL COMMENT 'Параметр резидентності клієнта  (1-резидент, 2-нерезидент)'," +
                "  Country INT NULL COMMENT 'Код країни  (001-999)'," +
                "  OznIns INT NULL COMMENT 'Ознака інсайдера '," +
                "  instSek INT NULL COMMENT 'Інституційний сектор економіки '," +
                "  UNIQUE INDEX AsNumber_UNIQUE (AsNumber ASC)," +
                "  UNIQUE INDEX PAN_UNIQUE (PAN ASC)," +
                "  INDEX Name (Name ASC)," +
                "  INDEX Passport (Passport ASC)," +
                "  INDEX IdCode (IDN_Podatk ASC)," +
                "  INDEX BirthDay (BirthDate ASC)," +
                "  PRIMARY KEY (PAN),CONSTRAINT AsNumb" +
                "    FOREIGN KEY (AsNumber)" +
                "    REFERENCES sbon.ACCOUNT (AsNumber)" +
                "    ON DELETE RESTRICT" +
                "    ON UPDATE CASCADE )" +
                "ENGINE = InnoDB;");
        s.executeUpdate("CREATE TABLE IF NOT EXISTS sbon.DOV_ANAL (" +
                "  AR_INVAL CHAR(14) NULL COMMENT 'Аналітичний рахунок в іноземній валюті'," +
                "  AR_NAVAL CHAR(14) NULL COMMENT 'Аналітичний рахунок в національні валюті'," +
                "  ShifrVkl CHAR(14) NULL COMMENT 'шифр депозиту згідно із DOV_VKL')" +
                "ENGINE = InnoDB;");
        s.executeUpdate("CREATE TABLE IF NOT EXISTS sbon.DOV_CONVOPER (" +
                "  Name VARCHAR(30) NULL COMMENT 'Назва бухгалтерської проводки'," +
                "  CodeOper INT NULL COMMENT 'Код операції '," +
                "  CodValFrom INT NULL COMMENT 'Код валюти'," +
                "  RateType ENUM('0', '1') NULL COMMENT 'Курс конвертації: 0 – НБУ, 1 – банку'," +
                "  ConvCodeOper INT NULL COMMENT 'Код операції для конвертації валюти'," +
                "  KodValTo INT NULL COMMENT 'Код валюти, у яку конвертується ')" +
                "ENGINE = InnoDB;");
        s.executeUpdate("CREATE TABLE IF NOT EXISTS sbon.DOV_ISEK (" +
                "  ParIsek INT NULL COMMENT 'Параметр  інституційного сектору економіки'," +
                "  ParUsek INT NULL COMMENT 'Параметр  узагальненого  сектору економіки'," +
                "  Nazva VARCHAR(50) NULL COMMENT 'Назва параметру')" +
                "ENGINE = InnoDB;");
        s.executeUpdate("CREATE TABLE IF NOT EXISTS sbon.DOV_SYMZ (" +
                "  sKasZv INT NULL COMMENT 'Код символу звіту '," +
                "  Nazva VARCHAR(50) NULL COMMENT 'Назва прибуткової або видаткової статті , що відповідає символу звіту'," +
                "  Oznaka INT  NULL COMMENT 'Ознака операції:  0 – прихід в касу, 1- видача з каси')" +
                "ENGINE = InnoDB;");
        s.executeUpdate("CREATE TABLE IF NOT EXISTS sbon.DOV_VAL (" +
                "  CodeR030 INT NULL COMMENT 'Числовий код валюти'," +
                "  GroupR031 ENUM('1', '2', '3') NULL COMMENT 'Група валют '," +
                "  GroupR032 ENUM('1', '2', '3', '9') NULL COMMENT 'Група валют  за групами країн'," +
                "  Nazva VARCHAR(30) NULL COMMENT 'Назва валют'," +
                "  LitCode CHAR(3) NULL COMMENT 'Літерний код'," +
                "  Konvert INT NULL COMMENT 'Ступінь конвертації'," +
                "  Suffics CHAR(5) NULL COMMENT 'Суфікс – ознака для виводу на друк'," +
                "  MinNom INT NULL COMMENT 'Мінімальний номінал')" +
                "ENGINE = InnoDB;");
        s.executeUpdate("CREATE TABLE IF NOT EXISTS sbon.DOV_VAL_POS (" +
                "  Name VARCHAR(50) NULL COMMENT 'Назва бухгалтерської проводки'," +
                "  Key_Account CHAR(14) NULL COMMENT 'Аналітичний рахунок'," +
                "  RateType ENUM('0', '1') NULL COMMENT 'Курс конвертації: 0 – НБУ, 1 – банку  '," +
                "  Dt_CodeOper INT NULL COMMENT 'Код операції еквівалентної проводки відносно дебету'," +
                "  Ct_CodeOper INT NULL COMMENT 'Код операції еквівалентної проводки відносно кредиту\'," +
                "  KodVal INT NULL COMMENT 'Код валюти, в яку конвертується')" +
                "ENGINE = InnoDB;");
        s.executeUpdate("CREATE TABLE IF NOT EXISTS sbon.DOV_VKL (" +
                "  Shifr INT UNSIGNED NOT NULL COMMENT 'Шифр вкладу'," +
                "  Nazva VARCHAR(50) NULL COMMENT 'Назва  вкладу,'," +
                "  ProcSt VARCHAR(10) NULL COMMENT 'Процентна ставка'," +
                "  VklTyp INT NULL COMMENT 'Тип вкладу:  0 – поточний (основний); 1 – строковий;  2 – чековий;  3 – картрахунок.'," +
                "  TypProcSt ENUM('1', '2') NULL COMMENT 'Тип процетної ставки (1 – фіксована, 2 – плаваюча) '," +
                "  BaseObProc ENUM('360', '365', '366') NULL COMMENT 'База обчислення процентів (360, 365, 366 днів)'," +
                "  KodVal INT NULL COMMENT 'Код валюти'," +
                "  KodStrok INT NULL COMMENT 'Код строку депозитних рахунків '," +
                "  AR_Vkl CHAR(14) NULL COMMENT 'Рахунок для обліку  прихідних / витратних операцій  за даним видом вкладу\'," +
                "  AR_nProc CHAR(14) NULL COMMENT 'Рахунок для обліку  нарахованих процентів для даного виду вкладу'," +
                "  AR_zProc CHAR(14) NULL COMMENT 'Рахунок для обліку  зарахованих процентів для даного виду вкладу. Якщо відсутній, то за замовчуванням використовується рахунок AR_Vkl'," +
                "  AR_operZat1 CHAR(14) NULL COMMENT 'Рахунок для обліку гривневого еквіваленту процентних витрат '," +
                "  AR_operZat2 CHAR(14) NULL COMMENT 'Рахунок для обліку процентних витрат нарахованих і Оподаткованих \'," +
                "  AR_operZat3 CHAR(14) NULL COMMENT 'Рахунок для обліку гривневого еквіваленту валютної позиції банку'," +
                "  AR_operZat4 CHAR(14) NULL COMMENT 'Рахунок для обліку процентних витрат зарахованих і Ооподаткованих'," +
                "  peNarProc CHAR(14) NULL COMMENT 'Періодичність нарахування процентів '," +
                "  peZarProc CHAR(14) NULL COMMENT 'Періодичність зарахування процентів'," +
                "  teZakVkl CHAR(14) NULL COMMENT 'Термін  закриття  вкладу.'," +
                "  PRIMARY KEY (Shifr)," +
                "  INDEX dov_vkl (Shifr ASC, KodVal ASC, VklTyp ASC, KodStrok ASC))" +
                "ENGINE = InnoDB;");
        s.executeUpdate("CREATE TABLE IF NOT EXISTS sbon.DOVSTROK (" +
                "  ParS180 INT NOT NULL COMMENT 'Код  S180  строку кредитних та депозитних рахунків'," +
                "  ParS181 INT NULL COMMENT 'Узагальнений код S181: \uF0B7       1 – короткостроковий; \uF0B7       2 – довгостроковий.'," +
                "  Nazva VARCHAR(30) NULL COMMENT 'Назва строку'," +
                "  PRIMARY KEY (ParS180))" +
                "ENGINE = InnoDB;");
        s.executeUpdate("CREATE TABLE IF NOT EXISTS sbon.KURS_VAL (" +
                "  KodVal INT NOT NULL COMMENT 'Код валюти'," +
                "  Kurs_Nbu FLOAT(11,7) NULL COMMENT 'Курс НБУ'," +
                "  Kurs_Pok FLOAT(7,3) NULL COMMENT 'Курс купівлі '," +
                "  Kurs_Prod FLOAT(7,3) NULL COMMENT 'Курс продажу'," +
                "  Koef INT NULL COMMENT 'Коефіцієнт курсу валют – співвідношення курсу до одиниці валюти'," +
                "  PRIMARY KEY (KodVal))" +
                "ENGINE = InnoDB;");
        s.executeUpdate("CREATE TABLE IF NOT EXISTS sbon.OPERJRNL (" +
                "  DocNumber INT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'Номер документу'," +
                "  DateDoc DATE NULL COMMENT 'Дата документу'," +
                "  MFO CHAR(6) NULL COMMENT 'МФО банку'," +
                "  ACCOUNT CHAR(14) NULL COMMENT 'Аналітичний рахунок, з якого списуються кошти'," +
                "  MFOK CHAR(6) NULL COMMENT 'МФО отримувача'," +
                "  ACCOUNTK CHAR(14) NULL COMMENT 'Аналітичний рахунок, на який зараховуються кошти'," +
                "  FlagDK INT NULL COMMENT 'Ознака проводки: 0 – дебетова; 1 - кредитова '," +
                "  Suma INT NULL COMMENT 'Сума операції'," +
                "  SumaEqui INT NULL COMMENT 'Сума у гривневому еквіваленті'," +
                "  Currency INT NULL COMMENT 'Код валюти'," +
                "  Skaszv INT NULL COMMENT 'Символ касового звіту для операцій з касою'," +
                "  Namepayer VARCHAR(50) NULL COMMENT 'Назва платника'," +
                "  Namepayee VARCHAR(50) NULL COMMENT 'Назва одержувача платежу'," +
                "  Namepayment VARCHAR(100) NULL COMMENT 'Призначення платежу'," +
                "  IdnClerk INT NULL COMMENT 'Ідентифікатор службовця'," +
                "  Status INT NULL COMMENT 'Статус проводки0 – виконана (сквитована)\n" +
                "1 – cформована для виконання\n" +
                "2 – в обробці\n" +
                "3 – відмова (існує помилка в проводці при формуванні для ОДБ)\n" +
                "4 – несквитована (не проведена в ОДБ)'," +
                "  NameFile CHAR(12) NULL COMMENT 'Назва файлу проводок для передачі в ОДБ\n'," +
                "  ZKPO CHAR(10) NULL COMMENT 'ЗКПО'," +
                "  ZKPOK CHAR(10) NULL COMMENT 'ЗКПО отримувача'," +
                "  PRIMARY KEY (DocNumber)," +
                "  UNIQUE INDEX NumOper_UNIQUE (DocNumber ASC)," +
                "  INDEX oper (DocNumber ASC, DateDoc ASC))" +
                "ENGINE = InnoDB;");
        Conn.close();

    }
}
