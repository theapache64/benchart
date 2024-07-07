![](cover.jpeg)

# 📊 benchart

> A web tool to visualize and compare plain text data with Android Macrobenchmark data support

![](https://img.shields.io/github/deployments/theapache64/benchart/github-pages)
<a href="https://twitter.com/theapache64" target="_blank">
<img alt="Twitter: theapache64" src="https://img.shields.io/twitter/follow/theapache64.svg?style=social" />
</a>


![virat-kohli-ben-stokes](https://github.com/theapache64/benchart/assets/9678279/01381728-1ae2-4124-a4d5-7cc3dd7ba910)

> Yeah, correct, [he](https://en.wikipedia.org/wiki/Virat_Kohli)'s saying "Benchart"

## ✨ Demo

https://user-images.githubusercontent.com/9678279/204081186-36c7ce90-6c46-4ec6-9c36-dd16cdf25acf.mov

## 🐣 Getting Started

Let's start with some sample log data to learn the basics.

![image](https://github.com/theapache64/benchart/assets/9678279/2cd4f30e-054a-4f1e-b0bf-1443319a50a4)

### 📄 Input

- Benchart accepts input as plain text.
- A block is a chunk of information that you can compare.
- Blocks are separated by a blank line.
- The first line of the block will be treated as the header line. 
  - In the above example, `# before` and `# after`.
- Remaining lines will be treated as input lines.
- In an input line, the last number is treated as the value, and anything before that as the key.
  - For example, given the input line `myFirstFunction() = 90ms`, the key will be `myFirstFunction`, and the value will be `90`.
- All special characters will be stripped out from all the lines (e.g., `(`, `)`, `=`, `#`).

### 📊 Visualization

- The X-axis of the chart is for keys.
- The Y-axis of the chart is for values.
- Headers will be given as legend.

### 💻 Result

- The result compares the blocks and shows 2 states:
  - `better`: a decrease in value.
  - `worse`: an increase in value.
- Each result line shows the change in percentage as well as the change in value.

## 🔃 The Swap

You can swap the blocks, and the results will also be swapped (see the above example to find the difference).

<img src="https://github.com/theapache64/benchart/assets/9678279/e76ba831-05e1-454a-b39f-6cf10a254b2d" width="500"/>

## 🧱 Multi-blocks

You can have `n` number of blocks. The first word of the header will be considered a group. When you have multiple blocks starting with the same word, the result will be the average of those blocks. See the example below.

![image](https://github.com/theapache64/benchart/assets/9678279/f3877308-2d03-4152-b7e1-aec2e244a29c)

## 👥 Auto Group

To color the same group with the same color on the chart, enable Auto Group.

![image](https://github.com/theapache64/benchart/assets/9678279/c33153ae-affc-4977-8c29-edb468054053)

## ➗ Auto Average

In a block, if there are multiple lines with the same key, they will be averaged.

![image](https://github.com/theapache64/benchart/assets/9678279/0a6901d0-e042-4f31-893a-0570c6d837e2)

## 🎯 Focus Group

You'll see a new element called Focus Group when auto average is performed.

![image](https://github.com/theapache64/benchart/assets/9678279/173591ea-018d-44a1-bc2b-8bd6d3dc69c8)

Selecting a group from the Focus Group dropdown will show each value in the chart.

![image](https://github.com/theapache64/benchart/assets/9678279/86cb663c-31c4-433e-b11b-61f9a1ce9e7c)

## 🤖 Android Support

You can paste your Macrobenchmark result data into the input box, and it'll draw custom charts for each metric.

![image](https://github.com/theapache64/benchart/assets/9678279/768babb9-7cff-4798-840e-46a13009734a)

## ⭐️ Star History

[![Star History Chart](https://api.star-history.com/svg?repos=theapache64/benchart&type=Date)](https://star-history.com/#theapache64/benchart&Date)

## ✍️ Author

👤 **theapache64**

* Twitter: <a href="https://twitter.com/theapache64" target="_blank">@theapache64</a>
* Email: theapache64@gmail.com

Feel free to ping me 😉

## 🤝 Contributing

Contributions are what make the open-source community such an amazing place to learn, inspire, and create. Any contributions you make are greatly appreciated.

1. Open an issue first to discuss what you would like to change.
1. Fork the project.
1. Create your feature branch (`git checkout -b feature/amazing-feature`).
1. Commit your changes (`git commit -m 'Add some amazing feature'`).
1. Push to the branch (`git push origin feature/amazing-feature`).
1. Open a pull request.

Please make sure to update tests as appropriate.

## ❤ Show Your Support

Give a ⭐️ if this project helped you!

<a href="https://www.patreon.com/theapache64">
  <img alt="Patron Link" src="https://c5.patreon.com/external/logo/become_a_patron_button@2x.png" width="160"/>
</a>

<a href="https://www.buymeacoffee.com/theapache64" target="_blank">
    <img src="https://cdn.buymeacoffee.com/buttons/v2/default-yellow.png" alt="Buy Me A Coffee" width="160">
</a>

## 📝 License
```
Copyright © 2024 - theapache64

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```

_This README was generated by [readgen](https://github.com/theapache64/readgen)_ ❤
