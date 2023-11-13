import * as React from 'react';
import styled from 'styled-components';
import { useReadReport } from '../../api/api';
import { useApplicationContext } from '../../context/applicationState';
import { ReportInfo } from '../../model';
import LogComponent from '../LogComponent';
import ContentContainer from './ContentContainer';

const ITEM_HEIGHT = 28;
const PAGE_SIZE = 20;
const HEIGHT = ITEM_HEIGHT * PAGE_SIZE;

interface IScrollerProps {
    report: ReportInfo;
}

const Container = styled.div`
    flex: 1 1 500px; 
    height: 100%;
    width: 100%;
    overflow: auto;
`;

const Envelope = styled.div<{ minHeight: number}>`
    position: relative;    
    width: 100%;
    ${({ minHeight }) => `height: ${minHeight}px;`}    
`;

const getCurrentIndex = (scrollTop: number, pageCount: number, heights: Map<number, number>) => {
    let value = scrollTop;
    for (let i = 0; i < pageCount; i++) {
        const pageHeight = heights.get(i) || HEIGHT;
        console.log(`index for scroll ${value} page ${i} height ${pageHeight}`);
        if (value - pageHeight < 0) {            
            return i;    
        }
        value -= pageHeight;
    }
    return 0;
    // throw new Error(`Unable to compute current page index`);
}

const getPageTop = (pageNumber: number, heights: Map<number, number>) => {
    let value = 0;
    for (let i = 0; i < pageNumber; i++) {          
        const pageHeight = heights.get(i) || HEIGHT;
        value += pageHeight;
    }
    return value;
}

const isEven = (value: number) => value % 2 === 0;

const getStartIndex = (page: number, pageSize: number) =>  page * pageSize;

const getEndIndex = (page: number, pageSize: number, pageCount: number, totalItems: number) =>  {
    const lastPage = page === pageCount - 1;
    if (lastPage) {
        return totalItems;
    } else {
        return getStartIndex(page, pageSize) + pageSize;
    }        
}    


const Scroller: React.FC<IScrollerProps> = ({
    report: { id, rootSpanCount }
}) => {
    const pageCount = React.useMemo(() => {
        let res = Math.floor(rootSpanCount / PAGE_SIZE); 
        if (res === 0) {
            return 1;
        } else {
            return res;
        }        
    }, []);            

    // console.log(`span count ${rootSpanCount}`);
    // console.log(`page count ${pageCount}`);
    // console.log(`height ${height}`);    


    const { state: { expandedSpanIds} } = useApplicationContext();
    const [heights, setHeights] = React.useState<Map<number, number>>(new Map());    
    const [even, setEven] = React.useState<number>(0);    
    const [odd, setOdd] = React.useState<number>(1);         

    const envelopeRef = React.useRef<HTMLInputElement>(null);
    const evenEnvelopeRef = React.useRef<HTMLInputElement>(null);
    const oddEnvelopeRef = React.useRef<HTMLInputElement>(null);
    const evenContainerRef = React.useRef<HTMLInputElement>(null);
    const oddContainerRef = React.useRef<HTMLInputElement>(null);

    React.useEffect(() => {        
        console.log(`even ${even} odd ${odd}`);
    }, [odd, even]); 

    React.useEffect(() => {        
        const lastPageHeight = (getEndIndex(pageCount-1 , PAGE_SIZE, pageCount, rootSpanCount) - getStartIndex(pageCount-1, PAGE_SIZE)) * ITEM_HEIGHT;
        console.log(`last page height ${lastPageHeight}`);
        heights.set(pageCount -1, lastPageHeight);
    }, []);            

    React.useEffect(() => {
        console.log(`even height ${evenContainerRef?.current?.offsetHeight}`);
        console.log(`odd height ${oddContainerRef?.current?.offsetHeight}`);
        if (evenContainerRef?.current?.offsetHeight) {
            heights.set(even, evenContainerRef?.current?.offsetHeight);
        }
        if (oddContainerRef?.current?.offsetHeight) {
            heights.set(odd, oddContainerRef?.current?.offsetHeight);
        }

        let total = 0;
        for (let i = 0; i < pageCount; i++) {
            // console.log(`page ${i} top ${total}`)
            if (evenEnvelopeRef.current && i === even) {
                evenEnvelopeRef.current.style.top = `${total}px`;
            }
            if (oddEnvelopeRef.current && i === odd) {
                oddEnvelopeRef.current.style.top = `${total}px`;
            }
            total += heights.get(i) || HEIGHT;
        }
        // console.log(`recomputed total height ${total}`);
        if (envelopeRef.current) {
            envelopeRef.current.style.height = `${total}px`;
        }

    }, [ expandedSpanIds ]);            

    const handleScroll = (e: any) => {
        let scrollTop = e.currentTarget.scrollTop;
        let currentIndex = getCurrentIndex(scrollTop, pageCount, heights);
        console.log(`currentIndex ${currentIndex}`);
        // const currentIndex = Math.min(pageCount, Math.floor(scrollTop / HEIGHT));
        if (isEven(currentIndex)) {
            setEven(currentIndex);
            if (currentIndex + 1 < pageCount) {
                setOdd(currentIndex + 1);   
            }  
        } else {
            setOdd(currentIndex);
            if (currentIndex + 1 < pageCount) {
                setEven(currentIndex + 1);  
            }   
        }
    };


    return (
        <Container className="container" onScroll={handleScroll}>
            <Envelope ref={envelopeRef} minHeight={ITEM_HEIGHT * rootSpanCount}>    
                <ContentContainer 
                    id={id} 
                    innerRef={evenContainerRef} 
                    envelopeRef={evenEnvelopeRef} 
                    startIndex={getStartIndex(even, PAGE_SIZE)} 
                    endIndex={getEndIndex(even, PAGE_SIZE, pageCount, rootSpanCount)} 
                    top={getPageTop(even, heights)}
                    pageNumber={even}
                />
                <ContentContainer 
                    id={id} 
                    innerRef={oddContainerRef} 
                    envelopeRef={oddEnvelopeRef} 
                    startIndex={getStartIndex(odd, PAGE_SIZE)} 
                    endIndex={getEndIndex(odd, PAGE_SIZE, pageCount, rootSpanCount)} 
                    top={getPageTop(odd, heights)}
                    pageNumber={odd}
                />
            </Envelope>
        </Container>
    )
}

export default Scroller;