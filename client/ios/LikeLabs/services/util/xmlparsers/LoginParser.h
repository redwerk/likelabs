#import <Foundation/Foundation.h>

@interface LoginParser : NSObject <NSXMLParserDelegate>

@property (nonatomic, assign) NSUInteger tabletId;
@property (nonatomic, retain) NSString* apiKey;

@end
