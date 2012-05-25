#import "SettingsParser.h"
#import "Review.h"

@interface SettingsParser()

@property (nonatomic, retain) NSMutableString* currentText;
@property (nonatomic, retain) NSMutableArray* promoReviewsCollector;
@property (nonatomic, retain) Review* currentReview;

@end

@implementation SettingsParser

@synthesize currentText = _currentText;
@synthesize companyName = _companyName;
@synthesize companyLogoUrl = _companyLogoUrl;
@synthesize promoReviews = _promoReviews;
@synthesize promoReviewsCollector = _promoReviewsCollector;
@synthesize currentReview = _currentReview;

- (void)parserDidStartDocument:(NSXMLParser *)parser {
    NSLog(@"start document");
}

- (void)parser:(NSXMLParser *)parser didStartElement:(NSString *)elementName namespaceURI:(NSString *)namespaceURI qualifiedName:(NSString *)qName attributes:(NSDictionary *)attributeDict {
    if ([elementName isEqualToString:@"sampleReviews"]) {
        _promoReviewsCollector = [[NSMutableArray alloc] initWithCapacity:50];
    } else if ([elementName isEqualToString:@"sampleReview"]) {
        _currentReview = [[Review alloc] init];
    }
}

- (void)parser:(NSXMLParser *)parser foundCharacters:(NSString *)string {
    if (!self.currentText) {
        _currentText = [[NSMutableString alloc] initWithCapacity:80];
    }
    [self.currentText appendString:string];
}

- (void)parser:(NSXMLParser *)parser didEndElement:(NSString *)elementName namespaceURI:(NSString *)namespaceURI qualifiedName:(NSString *)qName {
    if ([elementName isEqualToString:@"companyName"]) {
        self.companyName = self.currentText;
    } else if ([elementName isEqualToString:@"companyLogoUrl"]) {
        self.companyLogoUrl = self.currentText;
    } else if ([elementName isEqualToString:@"sampleReviews"]) {
        self.promoReviews = self.promoReviewsCollector;
    } else if ([elementName isEqualToString:@"sampleReview"]) {
        self.currentReview.reviewType = (self.currentReview.imageUrl) ? ReviewTypePhoto : ReviewTypeText;
        [self.promoReviewsCollector addObject:self.currentReview];
        self.currentReview = nil;
    } else if ([elementName isEqualToString:@"author"]) {
        self.currentReview.user.name = self.currentText;
    } else if ([elementName isEqualToString:@"text"]) {
        self.currentReview.text = self.currentText;
    } else if ([elementName isEqualToString:@"imageUrl"]) {
        self.currentReview.imageUrl = self.currentText;
    } 
    self.currentText = nil;
}

- (void)parserDidEndDocument:(NSXMLParser *)parser {
    NSLog(@"end document");
}

- (void)dealloc {
    self.currentText = nil;
    self.companyName = nil;
    self.companyLogoUrl = nil;
    self.promoReviewsCollector = nil;
    self.promoReviews = nil;
    self.currentReview = nil;
    [_currentReview release];
    [_promoReviews release];
    [_promoReviewsCollector release];
    [_companyLogoUrl release];
    [_currentText release];
    [_companyName release];
    [super dealloc];
}

@end
