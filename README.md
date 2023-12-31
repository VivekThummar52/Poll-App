Poll App developed using MVVM, Coroutines, LiveData, Data binding and Room Database

__Functionality and How app works:__
1) `MainActivity` with two fragments: `CurrentPollFragment()` and `HistoryFragment()`, a Fab button to launch and create a new Poll in an activity `CreatePollActivity`.
2) in `CreatePollActivity`, user can add a question and add up to 5 options.
3) After creating a new poll, it will be shown in `CurrentPollFragment()`, where user can select an option from the list of different polls.
4) If there's any option selected, and user navigated to the `HistoryFragment()` or will launch an activity `CreatePollActivity`, poll with any options selected, will be moved to the `History` from `Current`. If user exits an app after selecting any options, then also it will move to the `History` from `Current`.
5) If there's no option is selected, it will remain in the `CurrentPollFragment()`.

__Note:__ User can change their selection while poll is in `Current Poll Tab` and moving to History/CreatePollActivity or exiting an app will be considered as Submission as there's no separate button for Submission.