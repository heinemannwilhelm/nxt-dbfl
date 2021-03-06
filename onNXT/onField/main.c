#include "config.c"
#include "display.c"
#include "sensor.c"
#include "motor.c"
#include "physic.c"

#include "NXCDefs.h"
#include "BTlib.c"

#include "color.c"
#include "follow.c"
#include "rotate.c"
#include "onTouch.c"
#include "move.c"
#include "bluetooth.c"

task main()
{
bluetooth_init();
sensor_init();
display_start();

while(true)
{
onTouch();
if(!color_field()){
follow_line();
}else{
motor_stop();
bluetooth_run();
}

}
motor_stop();
}
