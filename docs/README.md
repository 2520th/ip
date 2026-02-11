# IP: Chat with Monika

This is a greenfield Java project where you can interact with Monika from the visual novel Doki Doki Literature Club. She will help you with tasks management as well.

## This application is very easy to use. Just

* Download a JAR release from [here](https://github.com/2520th/ip)
* Place Data folder in parallel to it
* Run with command prompt

## What can she do right now?

1. randomly greets you with some surprises 😉
2. understand dates and time formats
3. - [X] mark
   - [ ] unmark
   - [ ] list
   - [ ] delete
   - [ ] find

## Main method looks like this
```
    public static void main(String[] args) throws IOException {
        UI ui = new UI();
        greeting(ui);
        Storage storage = new Storage();
        TaskMgr tasks = storage.loadTasks();
        Parser parser = new Parser();
        Executor exe = new Executor();
        while (true) {
            Command cmd = parser.parse(ui.input());
            String s = exe.execute(cmd, tasks);
            if (s == null) {
                break;
            } else {
                ui.say(s);
            }
        }
        storage.storeTasks(tasks);
        ui.say(" Bye. Hope to see you again soon!");
    }
```
