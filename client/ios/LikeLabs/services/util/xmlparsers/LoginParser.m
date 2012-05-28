#import "LoginParser.h"

@interface LoginParser()

@property (nonatomic, retain) NSMutableString* currentText;

@end

@implementation LoginParser
@synthesize currentText = _currentText;
@synthesize tabletId = _tabletId;
@synthesize apiKey = _apiKey;

- (void)parserDidStartDocument:(NSXMLParser *)parser {
    NSLog(@"Parser did start document");
}

- (void)parser:(NSXMLParser *)parser foundCharacters:(NSString *)string {
    if (!self.currentText) {
        _currentText = [[NSMutableString alloc] initWithCapacity:80];
    }
    [self.currentText appendString:string];
}

- (void)parserDidEndDocument:(NSXMLParser *)parser {
    NSLog(@"Parser did end document");
}

- (void)parser:(NSXMLParser *)parser didEndElement:(NSString *)elementName namespaceURI:(NSString *)namespaceURI qualifiedName:(NSString *)qName {
    if ([elementName isEqualToString:@"id"]) {
        self.tabletId = self.currentText.integerValue;
        NSLog(@"tablet id: %d", self.tabletId);
    } else if ([elementName isEqualToString:@"apiKey"]) {
        self.apiKey = self.currentText;
        NSLog(@"apikey: %@", self.apiKey);
    }
    self.currentText = nil;
}

- (void)dealloc {
    self.currentText = nil;
    self.apiKey = nil;
    [_apiKey release];
    [_currentText release];
    [super dealloc];
}

@end
