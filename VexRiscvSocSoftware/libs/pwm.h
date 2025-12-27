#ifndef PWM_H_
#define PWM_H_

typedef struct
{
  volatile uint32_t TOP;
  volatile uint32_t COMPARE;
  volatile uint32_t DIVCONFIG;
  volatile uint32_t CONTROL;
} Pwm_Reg;

#endif // PWM_H_
