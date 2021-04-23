//package com.example.timetable;
//
//public class
//NoUse {
//
//    private void createLeftView(Course course) {
//        //动态生成课程表左侧的节数视图
//        int len = course.getEnd();
//        if (len > maxClassNumber) {
//            LinearLayout classNumberLayout = (LinearLayout) findViewById(R.id.class_number_layout);
//            View view;
//            TextView text;
//            for (int i = 0; i < len-maxClassNumber; i++) {
//                view = LayoutInflater.from(this).inflate(R.layout.class_number, null);
//                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(110,180);
//                view.setLayoutParams(params);
//                text = view.findViewById(R.id.class_number_text);
//                text.setText("" + number++);
//                classNumberLayout.addView(view);
//            }
//            maxClassNumber = len;
//        }
//    }
//
//    //创建卡片课程视图
//    private void createView(final Course course) {
//        int integer = course.getDay();
//        if ((integer < 1 && integer > 7) || course.getStart() > course.getEnd()) {
//            Toast.makeText(this, "星期几没写对,或课程结束时间比开始时间还早~~", Toast.LENGTH_LONG).show();
//        } else {
//            switch (integer) {
//                case 1: day = (RelativeLayout) findViewById(R.id.monday);break;
//                case 2: day = (RelativeLayout) findViewById(R.id.tuesday);break;
//                case 3: day = (RelativeLayout) findViewById(R.id.wednesday);break;
//                case 4: day = (RelativeLayout) findViewById(R.id.thursday);break;
//                case 5: day = (RelativeLayout) findViewById(R.id.friday);break;
//                case 6: day = (RelativeLayout) findViewById(R.id.saturday);break;
//                case 7: day = (RelativeLayout) findViewById(R.id.weekday);break;
//            }
//            final View view = LayoutInflater.from(this).inflate(R.layout.course_card, null); //加载单个课程布局
//            view.setY(180 * (course.getStart()-1)); //设置开始高度,即第几节课开始
//            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams
//                    (ViewGroup.LayoutParams.MATCH_PARENT,(course.getEnd()-course.getStart()+1)*180-2); //设置布局高度,即跨多少节课
//            view.setLayoutParams(params);
//            TextView text = view.findViewById(R.id.text_view);
//            text.setText(course.getCourseName() + "\n" + course.getTeacher() + "\n" + course.getClassRoom()); //显示课程名
//            day.addView(view);
//            //长按删除课程
//            view.setOnLongClickListener(new View.OnLongClickListener() {
//                @Override
//                public boolean onLongClick(View v) {
//                    view.setVisibility(View.GONE);//先隐藏
//                    day.removeView(view);//再移除课程视图
//                    SQLiteDatabase sqLiteDatabase =  databaseHelper.getWritableDatabase();
//                    sqLiteDatabase.execSQL("delete from course where course_name = ?", new String[] {course.getCourseName()});
//                    return true;
//                }
//            });
//        }
//}
