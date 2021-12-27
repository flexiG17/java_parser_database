USE Training_base_Kozhevnikov

CREATE TABLE data_about_course_from_csv (
	id INTEGER NOT NULL 
	CONSTRAINT key1 PRIMARY KEY,
	courseName TEXT NOT NULL, 
	maxScore INTEGER NOT NULL,
	courseGroup TEXT NOT NULL,
	student_id INTEGER NOT NULL
)

CREATE TABLE people_data_from_vk(
	personId INTEGER NOT NULL
	CONSTRAINT key2 PRIMARY KEY,
	name TEXT NOT NULL,
	surname TEXT NOT NULL,
	city TEXT NOT NULL,
	birthdate DATE,            
	image TEXT NOT NULL,
	vkId INTEGER NOT NULL,
	gender INTEGER DEFAULT 0 NOT NULL
)

CREATE TABLE student_data_from_csv(
	student_id INTEGER NOT NULL
	REFERENCES people_data_from_vk,
	course_id INTEGER NOT NULL
	REFERENCES data_about_course_from_csv
)

CREATE TABLE themes_in_course(
	theme_name TEXT NOT NULL,
	studentMaxPoint INTEGER NOT NULL,
	maxPoint INTEGER NOT NULL,
	course_id INTEGER NOT NULL
	REFERENCES data_about_course_from_csv,
	theme_id integer NOT NULL
	CONSTRAINT key3 PRIMARY KEY
)

CREATE TABLE task_in_course(
	id INTEGER NOT NULL
	CONSTRAINT key4 PRIMARY KEY,
	task_name TEXT NOT NULL,
	score INTEGER DEFAULT 0 NOT NULL,
	theme_id INTEGER NOT NULL
	REFERENCES themes_in_course,
	max_score INTEGER DEFAULT 0 NOT NULL
)
