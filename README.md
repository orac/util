## Shadowburst Java utilities

`com.shadowburst.util` is a package for things so useful you don't know why they're not in `java.util`. Currently that's only one class: `WeakList`.

## WeakList

`com.shadowburst.util.WeakList` is an implementation of the `java.util.List` interface that uses `WeakReference`s to avoid keeping its elements alive when they would otherwise be garbage-collected. Obviously this means that list indices are unstable, so you can't quite use it like a `List`, but it's much simpler than `java.util.WeakHashMap` for something like a list of callbacks.

## Software that uses this library
At present, two Shadowburst apps use this library:
* [The Hat Game](https://play.google.com/store/apps/details?id=com.shadowburst.hatgame)
* [Showr](https://play.google.com/store/apps/details?id=com.shadowburst.showr)

To see your project featured on this list, fork this repo, add your app to the list, and send a pull request.

## Licence

© 2012, 2013 Daniel Hulme t/a Shadowburst

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
