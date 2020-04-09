# Lockbox Local

A local implementation of [Pluckeye Lockbox](https://lockbox.pluckeye.net/help) made using Java and SQLite.

Pluckeye Lockbox is a web application that lets you store information in boxes that can only be accessed after a delay period, 
which is useful if you need to hide a password for help with self-control.

The problem is that the boxes in Pluckeye Lockbox self-destruct after a year, deleting the data you put in them.

**That means if you put an IOS password into a Pluckeye Lockbox and it self-destructed, you'd be locked out of your iPhone, with no way to get in.** That's where Lockbox Local comes in.


## Description

Lockbox Local performs the same functionality as Pluckeye Lockbox. Boxes are locally stored on your system instead of in the cloud, keeping your passwords under your control. That is, you can:

* Create and delete boxes
* Unlock and relock boxes
* Import and export boxes

The difference is that boxes do not self-destruct, so you can delay lock your passwords with no worries and no getting locked out of anything important to you.

### How do I export boxes?

Open Lockbox Local and click "Export boxes". Then, choose a directory in the dialog that appears. An .lbf file containing your boxes will be placed in.

### How do I import boxes?

Open Lockbox Local and click "Import boxes". Then, select an .lbf file. Every box in the file will be added to your boxes, as long as they have unique names.
