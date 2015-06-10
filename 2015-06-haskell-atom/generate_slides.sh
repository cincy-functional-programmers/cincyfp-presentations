#!/bin/sh

# Turning the Markdown source to slides requires Haskell and Pandoc:

pandoc -t s5 -s Slides.md -o Slides.html
