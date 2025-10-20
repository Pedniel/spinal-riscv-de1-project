RISCV de1_murax_franz
========================

The RISCV Softcore on the Terasic DE1 Board!

### Murax Plattform with RISCV Processor from SpinalHDL

  * 50 MHz Clockfrequency
  * UART with 115200 Baud
  * GPIO connected to the red LEDs
  * Timer

see:

  * https://github.com/SpinalHDL/VexRiscv
  * https://spinalhdl.github.io/SpinalDoc-RTD/dev/index.html
  
### Project Navigation

For convenience, navigate to the project root first, then set this alias:

```
cd ~/path/to/de1_murax_franz
alias cdproject="cd $(pwd)"
```

Now you can simply type `cdproject` from anywhere to return to the project base directory.

**Note:** This alias only persists for your current terminal session. To make it permanent, add it to your shell config:

If unkown, check which shell you're using

```
echo $SHELL
```

Then make the alias permanent for your shell:

```
echo 'alias cdproject="cd $(pwd)"' >> ~/.bashrc   # for bash

echo 'alias cdproject="cd $(pwd)"' >> ~/.zshrc   # for zsh
```

### VHDL from SpinalHDL

```
cdproject
cd VexRiscv
sbt "runMain vexriscv.demo.de1_murax_franz"
cp ./de1_murax_franz.vhd ../src/
```

### VHDL simulation of the processor

UART sends 'A' and LEDs switch after 1 second...

```
cdproject
cd sim
cd de1_murax_franz
make sim
```

### FPGA synthesis and download

```
cdproject
cd pnr
cd de1_murax_franz
make prog
```

### Software build with gcc

The code is already available as a finished Intel HEX file in the VexRiscV project. 
SpinalHDL then generates the VHDL ROM code from the Intel HEX format.
So, you don't necessarily have to compile the code. If you compile, you must
overwrite the Intel HEX file from the VexRiscV project.

```
cdproject
cd VexRiscvSocSoftware/projects/murax/demo
make
cp ./build/demo.hex ../../../../VexRiscv/src/main/ressource/hex/muraxDemo.hex
```

Then build VexRiscv again and run FPGA synthesis again. The code ends up in the FPGA via VHDL.

```
cdproject
cd VexRiscv
sbt "runMain vexriscv.demo.de1_murax_franz"
cp ./de1_murax_franz.vhd ../src/
```

### Connect UART to MAC

The UART runs at 115,200 baud. Using an FTDI USB UART adapter, you can connect the UART from the DE1 board. A message is sent, and the inputs are returned. On MacOS:

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

This is untested, but the JTAG Pins are on the Expansionport 1 of the DE1 Board.

```
GPIO_1[1] - JTAG_TCK
GPIO_1[3] - JTAG_TMS
GPIO_1[5] - JTAG_TDI
GPIO_1[7] - JTAG_TDO
```


