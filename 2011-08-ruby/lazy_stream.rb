require "enumerator"

module LazyStream
  class Node
    def initialize( head, &promise )
      @head, @tail = head, promise
    end

    attr_reader  :head

    def tail
      if @tail.is_a? Proc
        if @tail.arity == 1
          @tail.call(head)
        else
          @tail.call
        end
      else
        @tail
      end
    end

    def drop
      result, next_stream = head, tail
      @head, @tail = case next_stream
                     when self.class
                       next_stream.instance_eval {
                         [@head, @tail]
                       }
                     else
                       [next_stream, @tail]
                     end
      result
    end
    alias_method :tail!, :drop

    def end?
      @tail.nil?
    end
    def next?
      ! end?
    end

    def each!(limit=nil)
      loop do
        break unless limit.nil? || (limit -= 1) > -1
        yield drop
        break if end?
      end
      self
    end

    def each(limit=nil, &block)
      clone.each!(limit, &block)
      self
    end
    include Enumerable

    def limit(max_depth=nil)
      enum_for(:each, max_depth)
    end
    def limit!(max_depth=nil)
      enum_for(:each!, max_depth)
    end

  end
end

module Kernel
  def lazy_stream( *args, &block )
    LazyStream::Node.new(*args, &block)
  end
end
