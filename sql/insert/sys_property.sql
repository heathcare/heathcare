INSERT INTO sys_property (id_, category_, description_, initvalue_, inputtype_, locked_, maxvalue_, minvalue_, name_, title_, type_, value_) VALUES ('101', 'RPT', '�����6λ���¸�ʽ��YYYYMM������201312', '${curr_yyyymm}', null, 0, null, null, 'curr_yyyymm', '��ǰ����', null, '');
INSERT INTO sys_property (id_, category_, description_, initvalue_, inputtype_, locked_, maxvalue_, minvalue_, name_, title_, type_, value_) VALUES ('102', 'RPT', '�����8λ���ڸ�ʽ��YYYYMMDD������20130630', '${curr_yyyymmdd}', null, 0, null, null, 'curr_yyyymmdd', '��ǰ����', null, '');
INSERT INTO sys_property (id_, category_, description_, initvalue_, inputtype_, locked_, maxvalue_, minvalue_, name_, title_, type_, value_) VALUES ('103', 'RPT', '�����ļ�·��', null, null, 0, null, null, 'dataDir', '�����ļ�·��', null, '');
INSERT INTO sys_property (id_, category_, description_, initvalue_, inputtype_, locked_, maxvalue_, minvalue_, name_, title_, type_, value_) VALUES ('104', 'RPT', '�����������£�${curr_yyyymm}��ʾ��ǰ�·�', '', null, 0, null, null, 'input_yyyymm', '������������', null, '');
INSERT INTO sys_property (id_, category_, description_, initvalue_, inputtype_, locked_, maxvalue_, minvalue_, name_, title_, type_, value_) VALUES ('105', 'RPT', '������������ڣ�${curr_yyyymmdd}-1��ʾ��ǰ���ڵ�ǰһ��', '', null, 0, null, null, 'input_yyyymmdd', '������������', null, '');
INSERT INTO sys_property (id_, category_, description_, initvalue_, inputtype_, locked_, maxvalue_, minvalue_, name_, title_, type_, value_) VALUES ('106', 'RPT', '�������ɸ�Ŀ¼', null, null, 0, null, null, 'report_save_path', '�������ɸ�Ŀ¼', null, '');
INSERT INTO sys_property (id_, category_, description_, initvalue_, inputtype_, locked_, maxvalue_, minvalue_, name_, title_, type_, value_) VALUES ('TOKEN', 'SYS', null, null, null, 0, null, null, 'TOKEN', 'TOKEN', 'String', '4310374942be7e324e47bdfae8139dcaff137128');
INSERT INTO sys_property (id_, category_, description_, initvalue_, inputtype_, locked_, maxvalue_, minvalue_, name_, title_, type_, value_) VALUES ('1', 'SYS', 'ϵͳ����', null, null, 0, null, null, 'res_system_name', 'ϵͳ����', null, '���׻�������������Ϣϵͳ');
INSERT INTO sys_property (id_, category_, description_, initvalue_, inputtype_, locked_, maxvalue_, minvalue_, name_, title_, type_, value_) VALUES ('11', 'SYS', '���ò�ѯ����', '[{"name":"��","value":"true"},{"name":"��","value":"false"}]', 'combobox', 0, null, null, 'use_query_cache', '���ò�ѯ����', null, 'true');
INSERT INTO sys_property (id_, category_, description_, initvalue_, inputtype_, locked_, maxvalue_, minvalue_, name_, title_, type_, value_) VALUES ('12', 'SYS', 'ϵͳӢ������', null, null, null, null, null, 'res_system_name_en', 'ϵͳӢ������', null, '');
INSERT INTO sys_property (id_, category_, description_, initvalue_, inputtype_, locked_, maxvalue_, minvalue_, name_, title_, type_, value_) VALUES ('131', 'SYS', '��������ģʽ', '[{"name":"��","value":"true"},{"name":"��","value":"false"}]', 'combobox', 0, null, null, 'debugModeEnable', '��������ģʽ', null, 'true');
INSERT INTO sys_property (id_, category_, description_, initvalue_, inputtype_, locked_, maxvalue_, minvalue_, name_, title_, type_, value_) VALUES ('132', 'SYS', '���÷�����־', '[{"name":"��","value":"true"},{"name":"��","value":"false"}]', 'combobox', 0, null, null, 'enableAccessLog', '���÷�����־', null, 'true');
INSERT INTO sys_property (id_, category_, description_, initvalue_, inputtype_, locked_, maxvalue_, minvalue_, name_, title_, type_, value_) VALUES ('14', 'SYS', null, null, null, 0, null, null, 'res_mail_to_dev', '���ⱨ���������', null, 'jior2008@163.com');
INSERT INTO sys_property (id_, category_, description_, initvalue_, inputtype_, locked_, maxvalue_, minvalue_, name_, title_, type_, value_) VALUES ('2', 'SYS', 'ϵͳ�汾', null, null, 0, null, null, 'res_version', 'ϵͳ�汾', null, 'V3.0');
INSERT INTO sys_property (id_, category_, description_, initvalue_, inputtype_, locked_, maxvalue_, minvalue_, name_, title_, type_, value_) VALUES ('218', 'SYS', '�����ļ�����', '[{"name":"��","value":"true"},{"name":"��","value":"false"}]', 'combobox', 0, null, null, 'use_file_cache', '�����ļ�����', null, 'true');
INSERT INTO sys_property (id_, category_, description_, initvalue_, inputtype_, locked_, maxvalue_, minvalue_, name_, title_, type_, value_) VALUES ('3', 'SYS', '��Ȩ��Ϣ', null, null, 0, null, null, 'res_copyright', '��Ȩ��Ϣ', null, 'XXXX ��Ȩ����      ');
INSERT INTO sys_property (id_, category_, description_, initvalue_, inputtype_, locked_, maxvalue_, minvalue_, name_, title_, type_, value_) VALUES ('4', 'SYS', null, null, null, 0, null, null, 'res_mail_from', '�ʼ�������', null, 'admin@localhost');
INSERT INTO sys_property (id_, category_, description_, initvalue_, inputtype_, locked_, maxvalue_, minvalue_, name_, title_, type_, value_) VALUES ('5', 'SYS', '�޶�ÿ���˺�ֻ����һ����¼', '[{"name":"����","value":"true"},{"name":"������","value":"false"}]', 'combobox', 0, null, null, 'login_limit', 'ϵͳ��¼�˺�����', null, 'false');
INSERT INTO sys_property (id_, category_, description_, initvalue_, inputtype_, locked_, maxvalue_, minvalue_, name_, title_, type_, value_) VALUES ('6', 'SYS', '�����û����ʱ�䣨�룩', null, null, 0, null, null, 'login_time_check', '�����û����ʱ�䣨�룩', null, '600');
INSERT INTO sys_property (id_, category_, description_, initvalue_, inputtype_, locked_, maxvalue_, minvalue_, name_, title_, type_, value_) VALUES ('72', 'SYS', '��ҳLOGOͼƬ', null, 'imgbox', 0, null, null, 'res_logo', '��ҳLOGOͼƬ', null, '/static/images/logo.png');
INSERT INTO sys_property (id_, category_, description_, initvalue_, inputtype_, locked_, maxvalue_, minvalue_, name_, title_, type_, value_) VALUES ('74', 'SYS', '��¼ҳLOGOͼƬ', null, 'imgbox', 0, null, null, 'res_login_logo', '��¼ҳLOGOͼƬ', null, '/static/images/logo.png');
INSERT INTO sys_property (id_, category_, description_, initvalue_, inputtype_, locked_, maxvalue_, minvalue_, name_, title_, type_, value_) VALUES ('80', 'SYS', 'ϵͳ��ҳPortal����', '/mx/user/portal', null, 0, null, null, 'index_portal_link', 'ϵͳ��ҳPortal����', null, '');
INSERT INTO sys_property (id_, category_, description_, initvalue_, inputtype_, locked_, maxvalue_, minvalue_, name_, title_, type_, value_) VALUES ('88', 'SYS', '���ò������ʿ��ƣ����Ʋ�����������', '[{"name":"��","value":"true"},{"name":"��","value":"false"}]', 'combobox', 0, null, null, 'concurrentAccessLimit', '�������ʿ���', null, 'false');
INSERT INTO sys_property (id_, category_, description_, initvalue_, inputtype_, locked_, maxvalue_, minvalue_, name_, title_, type_, value_) VALUES ('9', 'SYS', '�ļ��洢��ʽ', '[{"name":"���ݿ�","value":"DATABASE"},{"name":"Ӳ��","value":"DISK"}]', 'combobox', 0, null, null, 'fs_storage_strategy', '�ļ��洢��ʽ', null, 'DATABASE');
INSERT INTO sys_property (id_, category_, description_, initvalue_, inputtype_, locked_, maxvalue_, minvalue_, name_, title_, type_, value_) VALUES ('INT_TOKEN', 'SYS', null, null, null, 0, null, null, 'INT_TOKEN', 'INT_TOKEN', 'String', '2018');
INSERT INTO sys_property (id_, category_, description_, initvalue_, inputtype_, locked_, maxvalue_, minvalue_, name_, title_, type_, value_) VALUES ('RunEnvironment', 'SYS', 'ϵͳ���л���', '[{"name":"��������","value":"dev"},{"name":"���Ի���","value":"test"},{"name":"��������","value":"product"}]', 'combobox', 0, null, null, 'RunEnvironment', 'ϵͳ���л���', null, 'product');
INSERT INTO sys_property (id_, category_, description_, initvalue_, inputtype_, locked_, maxvalue_, minvalue_, name_, title_, type_, value_) VALUES ('enableSmsRegVerification', 'SYS', '����ע�������֤', '[{"name":"��","value":"true"},{"name":"��","value":"false"}]', 'combobox', 0, null, null, 'enableSmsRegVerification', '����ע�������֤', null, 'false');
INSERT INTO sys_property (id_, category_, description_, initvalue_, inputtype_, locked_, maxvalue_, minvalue_, name_, title_, type_, value_) VALUES ('tabmax', 'SYS', '��ҳѡ������', null, null, 0, null, null, 'tabmax', '��ҳѡ������', null, '10');
INSERT INTO sys_property (id_, category_, description_, initvalue_, inputtype_, locked_, maxvalue_, minvalue_, name_, title_, type_, value_) VALUES ('enableAutoReg', 'SYS', '��������ע��', '[{"name":"��","value":"true"},{"name":"��","value":"false"}]', 'combobox', 0, null, null, 'enableAutoReg', '��������ע��', null, 'false');
INSERT INTO sys_property (id_, category_, description_, initvalue_, inputtype_, locked_, maxvalue_, minvalue_, name_, title_, type_, value_) VALUES ('651', 'SYS', null, null, null, 0, null, null, 'serviceUrl', '�����ַ', null, '');