# Daily Cost

>An android app to help you record daily cost and income.

## Overview

This is an elaborate app for recording daily expenses and income. It has simple but useful functions. The main idea is to help users to keep a record of their daily expenses. Users can choose what kind of expense/income they generate when it happened, and the amount of money they spent/got. After that, the home page will update with the total sum of pays and earns of each month and list all the money records in the chronological order from recent to past. 

To see more details about the cash flow, click the statistic button in the top right corner. With the bar chart by time sequence and pie chart by the different type of expense, you can easily know when you spend money most, and what kind of thing cost you most. In the "Reports" label, click an expense type and you can check this type of expenses by time order or amount order. Users also have easy access to manage their records. Just long-press on the record, and you can choose to modify or delete this record from the pop-up tab. 

Also, users can manage the type of expenses. From setting (top left corner)->Type management, you can change the name and icon of each kind of expense, or add a new one from the pre-designed database. In the Sort option, you can long-press the items to sort them.

Other features include backup data to the SD card and restore as well. Also, users can log in with multiple ways, like Google account, Facebook, email or as a guest. The libraries used in this app is listed in the Open source license tab.

Discover more when you use it!


## Architecture and technology

Architecture is from Google [Architecture Components](https://developer.android.com/jetpack/docs/guide)，including Lifecycle、LiveData 和 ViewModel. The database is using [Room](https://developer.android.com/topic/libraries/architecture/room).

Room and RxJava are really convenient to use together.

The data returned by Room is wrapped in Flowable and is observed in Activity using ViewModel. When the `record` table in the database changes, there is no need to update the View manually, and the ViewModel will automatically trigger the callback.

For example, the list interface displays all the data in the `record` table. We modified a certain piece of data in the `record` table in the detail interface. The list interface will automatically call back the data change method instead of manually requesting `record` Table all the data to refresh the interface.


## Related Links：

1. Architecture Components: https://developer.android.com/jetpack/docs/guide
2. Room: https://developer.android.com/topic/libraries/architecture/room