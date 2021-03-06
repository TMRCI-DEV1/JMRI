# Test for jmri_bindings_test.py

# load the shortcuts
exec( open("jython/jmri_bindings.py3").read() )

# check existence of standard values
sensors
turnouts
lights
signals
masts
routes
blocks
reporters
memories
powermanager
addressedProgrammers
globalProgrammers
dcc
audio
shutdown
layoutblocks
warrants

CLOSED
THROWN
CABLOCKOUT
PUSHBUTTONLOCKOUT
UNLOCKED
LOCKED

ACTIVE
INACTIVE

ON
OFF

UNKNOWN
INCONSISTENT

DARK
RED
YELLOW
GREEN
LUNAR
FLASHRED
FLASHYELLOW
FLASHGREEN
FLASHLUNAR

# check some values
import jmri

if (GREEN != jmri.SignalHead.GREEN) : raise AssertionError('GREEN Failed')
if (YELLOW != jmri.SignalHead.YELLOW) : raise AssertionError('YELLOW Failed')
if (RED != jmri.SignalHead.RED) : raise AssertionError('RED Failed')

if (ON != jmri.DigitalIO.ON) : raise AssertionError('ON Failed')
if (OFF != jmri.DigitalIO.OFF) : raise AssertionError('OFF Failed')

# here is success
