# Lockbox Local

This is a local implementation of [Pluckeye Lockbox](https://lockbox.pluckeye.net/help), made using Java and SQLite.

[Pluckeye Lockbox](https://lockbox.pluckeye.net/help) is a web application that lets you store information in "boxes" which can only be accessed after a delay period. This is useful if you need to hide a password from yourself, for help with self-control.

Pluckeye Lockbox has been around for years, but it's a free service run by one person (Jon). He warns:

"[Pluckeye Lockbox boxes] threaten to destruct mainly so that users do not assume the service will be around forever. You should not assume your data will be kept longer than 1 year, because the service is 100% free and run by one person. But, in practice, boxes frequently last longer than they claim to." [(Source.)](https://www.reddit.com/r/pluckeye/comments/mvyvmw/lockbox_i_typed_never_into_the_self_destruct_date/)

Still: If you stored your iPhone password in a Pluckeye Lockbox, and the Pluckeye Lockbox server crashed, and all data was lost, you'd be locked out of your iPhone, with no way to get in.

So, you might want to store an extra copy of your most valuable passwords on your own computer, using Lockbox Local.

## Description

Lockbox Local performs the same functionality as Pluckeye Lockbox. Each box is stored locally on your own computer, instead of in the cloud. This keeps your passwords under your control. You can:

* Create and delete boxes.
* Unlock and relock boxes.
* Import and export boxes.

Boxes do not self-destruct, so you can delay-lock your passwords with no worries of permanently losing them.

### How to export boxes

Open Lockbox Local and click "Export boxes". Then, choose a directory in the dialog that appears. An .lbf file containing your boxes will be placed in the directory.

### How to import boxes

Open Lockbox Local and click "Import boxes". Then, select an .lbf file. Every box in the file will be added to your boxes, as long as they have unique names.

## Download links

* [Windows release (64-bit)](https://github.com/japierreSWE/Lockbox_Local/releases/download/0.9/Lockbox_Local_WIN64.zip)
* [Mac release](https://github.com/japierreSWE/Lockbox_Local/releases/download/0.9/Lockbox.Local_Mac.zip)
