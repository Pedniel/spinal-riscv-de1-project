# PWM in style of RP2350 (simplified)


# Install prerequisites:

``` sh
    sudo apt update
    sudo apt install sbt
    sudo apt install gtkwave
```

At the time of writing this, Verilator 5 just released to apt Repos. However, Verilator 4 is still needed for this. If you can't find a binary, build with:

``` sh
sudo apt install -y git make autoconf g++ flex bison

git clone https://github.com/verilator/verilator
cd verilator
git checkout v4.228

autoconf
./configure
make -j$(nproc)
sudo make install

```

# Build

Build by using:

``` sh
    sbt "runMain FullPwmSim"
```

# Simulate with gtkwave by using:

``` sh
   gtkwave simWorkspace/PwmSystem/test/wave.vcd
```

# Waveform example:

Small part of the simulation, for better viewing:

<img src="img/sim-snippet1.png" width="700">

Full simulation screenshot:

<img src="img/sim-full1.png" width="700">
