require 'rubygems'
require 'memoize'
require 'benchmark'
include Benchmark

bm(14) do |x|
  class Integer
    def fib_i
      a, b = 0, 1
      self.times do
        a, b = b, a+b
      end
      a
    end

    def fib_r
      if self < 2
        self
      else
        (self - 2).fib_r + (self - 1).fib_r
      end
    end

    def fib_t
      fib_t_helper(self, 0, 1)
    end

    def fib_t_helper(n, a, b)
      if n.zero?
        a
      else
        fib_t_helper(n-1, b, a+b)
      end
    end

    @@fibs = [0,1]
    def fib_m
      @@fibs[self] ||= ((self - 2).fib_m +
                        (self - 1).fib_m)
    end

    @@fib_h = Hash.new{|h,k| h[k] = h[k-2] + h[k-1]}
    @@fib_h[0] = 0
    @@fib_h[1] = 1
    def fib_h
      @@fib_h[self]
    end
  end
  x.report("iteration 1000") {1000.times {|i| i.fib_i } }
  x.report("recursion 30  ") {  30.times {|i| i.fib_r } }
  x.report("recursion 40  ") {  40.times {|i| i.fib_r } }
  x.report("tail-rec. 1000") {1000.times {|i| i.fib_t } }
  x.report("memoized  40  ") {  40.times {|i| i.fib_m } }
  x.report("memoized  1000") {1000.times {|i| i.fib_m } }
end
