/**
 * Generated by orval v6.11.1 🍺
 * Do not edit manually.
 * Integra API - OpenAPI 3.0
 * This is ....
 * OpenAPI spec version: 1.0.11
 */
import {
  useQuery,
  useMutation
} from 'react-query'
import type {
  UseQueryOptions,
  UseMutationOptions,
  QueryFunction,
  MutationFunction,
  UseQueryResult,
  QueryKey
} from 'react-query'
import type {
  ReportInfo,
  ParseReportBody,
  Report,
  SpanDetail
} from '../model'
import { customInstance } from './custom-instance';
import type { ErrorType } from './custom-instance';


/**
 * @summary Uploads content
 */
export const parseReport = (
    parseReportBody: ParseReportBody,
 ) => {const formData = new FormData();
if(parseReportBody.file !== undefined) {
 formData.append('file', parseReportBody.file)
 }

      return customInstance<ReportInfo>(
      {url: `/api/v1/report`, method: 'post',
      headers: {'Content-Type': 'multipart/form-data', },
       data: formData
    },
      );
    }
  


    export type ParseReportMutationResult = NonNullable<Awaited<ReturnType<typeof parseReport>>>
    export type ParseReportMutationBody = ParseReportBody
    export type ParseReportMutationError = ErrorType<unknown>

    export const useParseReport = <TError = ErrorType<unknown>,
    
    TContext = unknown>(options?: { mutation?:UseMutationOptions<Awaited<ReturnType<typeof parseReport>>, TError,{data: ParseReportBody}, TContext>, }
) => {
      const {mutation: mutationOptions} = options ?? {};

      


      const mutationFn: MutationFunction<Awaited<ReturnType<typeof parseReport>>, {data: ParseReportBody}> = (props) => {
          const {data} = props ?? {};

          return  parseReport(data,)
        }

        

      return useMutation<Awaited<ReturnType<typeof parseReport>>, TError, {data: ParseReportBody}, TContext>(mutationFn, mutationOptions);
    }
    
/**
 * @summary Reads single report
 */
export const readReport = (
    reportId: string,
 signal?: AbortSignal
) => {
      return customInstance<Report>(
      {url: `/api/v1/report/${reportId}`, method: 'get', signal
    },
      );
    }
  

export const getReadReportQueryKey = (reportId: string,) => [`/api/v1/report/${reportId}`];

    
export type ReadReportQueryResult = NonNullable<Awaited<ReturnType<typeof readReport>>>
export type ReadReportQueryError = ErrorType<unknown>

export const useReadReport = <TData = Awaited<ReturnType<typeof readReport>>, TError = ErrorType<unknown>>(
 reportId: string, options?: { query?:UseQueryOptions<Awaited<ReturnType<typeof readReport>>, TError, TData>, }

  ):  UseQueryResult<TData, TError> & { queryKey: QueryKey } => {

  const {query: queryOptions} = options ?? {};

  const queryKey =  queryOptions?.queryKey ?? getReadReportQueryKey(reportId);

  


  const queryFn: QueryFunction<Awaited<ReturnType<typeof readReport>>> = ({ signal }) => readReport(reportId, signal);


  

  const query = useQuery<Awaited<ReturnType<typeof readReport>>, TError, TData>(queryKey, queryFn, {enabled: !!(reportId), ...queryOptions}) as  UseQueryResult<TData, TError> & { queryKey: QueryKey };

  query.queryKey = queryKey;

  return query;
}


/**
 * @summary Reads report span
 */
export const readSpan = (
    reportId: string,
    spanId: string,
 signal?: AbortSignal
) => {
      return customInstance<SpanDetail>(
      {url: `/api/v1/report/${reportId}/span/${spanId}`, method: 'get', signal
    },
      );
    }
  

export const getReadSpanQueryKey = (reportId: string,
    spanId: string,) => [`/api/v1/report/${reportId}/span/${spanId}`];

    
export type ReadSpanQueryResult = NonNullable<Awaited<ReturnType<typeof readSpan>>>
export type ReadSpanQueryError = ErrorType<unknown>

export const useReadSpan = <TData = Awaited<ReturnType<typeof readSpan>>, TError = ErrorType<unknown>>(
 reportId: string,
    spanId: string, options?: { query?:UseQueryOptions<Awaited<ReturnType<typeof readSpan>>, TError, TData>, }

  ):  UseQueryResult<TData, TError> & { queryKey: QueryKey } => {

  const {query: queryOptions} = options ?? {};

  const queryKey =  queryOptions?.queryKey ?? getReadSpanQueryKey(reportId,spanId);

  


  const queryFn: QueryFunction<Awaited<ReturnType<typeof readSpan>>> = ({ signal }) => readSpan(reportId,spanId, signal);


  

  const query = useQuery<Awaited<ReturnType<typeof readSpan>>, TError, TData>(queryKey, queryFn, {enabled: !!(reportId && spanId), ...queryOptions}) as  UseQueryResult<TData, TError> & { queryKey: QueryKey };

  query.queryKey = queryKey;

  return query;
}

