require File.expand_path(File.join(File.dirname(__FILE__), '..', '..', 'spec_helper'))

# run the specs once with a loaded association and once not
[ false, true ].each do |loaded|
  describe 'One to Many Associations' do
    extend DataMapper::Spec::CollectionHelpers::GroupMethods

    self.loaded = loaded

    # define the model prior to supported_by
    before :all do
      module ::Blog
        class Author
          include DataMapper::Resource

          property :id,   Serial
          property :name, String

          has n, :articles
        end

        class Article
          include DataMapper::Resource

          property :id,      Serial
          property :title,   String, :nullable => false
          property :content, Text

          belongs_to :author, :nullable => true
          belongs_to :original, self, :nullable => true
          has n, :revisions, self, :child_key => [ :original_id ]
          has 1, :previous,  self, :child_key => [ :original_id ], :order => [ :id.desc ]
        end
      end

      @author_model  = Blog::Author
      @article_model = Blog::Article
    end

    supported_by :all do
      before :all do
        @author  = @author_model.create(:name => 'Dan Kubb')

        @original = @author.articles.create(:title => 'Original Article')
        @article  = @author.articles.create(:title => 'Sample Article', :content => 'Sample', :original => @original)
        @other    = @author.articles.create(:title => 'Other Article',  :content => 'Other')

        # load the targets without references to a single source
        load_collection = lambda do |query|
          @author_model.get(*@author.key).articles(query)
        end

        @articles       = load_collection.call(:title => 'Sample Article')
        @other_articles = load_collection.call(:title => 'Other Article')

        @articles.entries if loaded
      end

      it_should_behave_like 'A public Collection'
      it_should_behave_like 'A public Association Collection'
      it_should_behave_like 'A Collection supporting Strategic Eager Loading'
    end
  end
end