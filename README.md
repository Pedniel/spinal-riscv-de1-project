RISCV de1_murax_franz
========================

Der fabelhafte RISCV Softcore auf dem Terasic DE1 Board! Für Franz.

### Murax Plattform mit RISCV Prozessor aus SpinalHDL

  * 50 MHz Taktfrequenz
  * UART mit 115200 Baud
  * GPIO an den Roten LEDs
  * Timer

siehe:

  * https://github.com/SpinalHDL/VexRiscv
  * https://spinalhdl.github.io/SpinalDoc-RTD/dev/index.html

### VHDL aus SpinalHDL

```
cd VexRiscV
sbt "runMain vexriscv.demo.de1_murax_franz"
cp ./de1_murax_franz.vhd ../src/
```

### VHDL Simulation des Prozessors

UART sendet 'A' und LEDs wechseln nach 1 Sekunde...

```
cd sim
cd de1_murax_franz
make sim
```

### FPGA Synthese und Download

```
cd pnr
cd de1_murax_franz
make prog
```

### Software Build mit gcc

Der Code liegt schon als fertiges Intel HEX im VexRiscV Projekt. SpinalHDL
generiert dann aus dem Intel HEX Format den VHDL ROM Code. Man muss also
nicht unbedingt den Code kompilieren. Wenn man kompiliert, dann muss das
Intel HEX File aus dem VexRiscV Projekt überschrieben werden.


```
cd VexRiscvSocSoftware/projects/murax/demo
make
cp ./build/demo.hex ../../../../VexRiscv/src/main/ressource/hex/muraxDemo.hex
```

Dann nochmal VexRiscV bauen und nochmal FPGA Synthese. Der Code landet via VHDL im
FPGA.

### UART anschliessen an MAC

Die UART läuft mit 115200 Baud. Mit einem FTDI USB UART Adapter kann man die UART vom DE1 Board anschliessen. Es wird eine Nachricht gesendet und die Eingaben kommen zurück. Auf MacOS:

```
screen /dev/tty.usbserial-FTALDMJL 115200
```

### Installation VexRiscV / SpinalHDL

see: https://github.com/SpinalHDL/VexRiscv#dependencies

SpinalHDL is a Scala application. Scala runs on the JAVA virtual machine.

```
sudo apt install openjdk-8-jdk
```

The  SCALA build tool "sbt" will install all dependencies for the VexRiscV for example SpinalHDL
locally. This is similar to pythons pip mechanism.

```
echo "deb https://repo.scala-sbt.org/scalasbt/debian all main" | sudo tee /etc/apt/sources.list.d/sbt.list
curl -sL "https://keyserver.ubuntu.com/pks/lookup?op=get&search=0x2EE0EA64E40A89B84B2DF73499E82A75642AC823" | sudo apt-key add
sudo apt update
sudo apt install sbt
```

### Installation GCC Compiler


```
wget -O riscv.tar.gz  https://static.dev.sifive.com/dev-tools/freedom-tools/v2020.12/riscv64-unknown-elf-toolchain-10.2.0-2020.12.8-x86_64-linux-ubuntu14.tar.gz
tar -xvzf riscv.tar.gz
sudo mv riscv64-unknown-elf-toolchain-10.2.0-2020.12.8-x86_64-linux-ubuntu14 /opt/riscv
export PATH="$PATH:/opt/riscv/bin"
cd VexRiscvSocSoftware/projects/murax/demo
make
```

### Debug mit OpenOCD

Habe ich nicht ausprobiert, aber die JTAG Pins sind auf dem Expansionport 1 vom DE1 Board.

```
GPIO_1[1] - JTAG_TCK
GPIO_1[3] - JTAG_TMS
GPIO_1[5] - JTAG_TDI
GPIO_1[7] - JTAG_TDO
```


